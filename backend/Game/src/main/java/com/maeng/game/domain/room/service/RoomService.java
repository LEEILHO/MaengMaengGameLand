package com.maeng.game.domain.room.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.exception.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maeng.game.domain.awrsp.exception.GameSettingException;
import com.maeng.game.domain.awrsp.service.AwrspService;
import com.maeng.game.domain.gsb.service.GsbService;
import com.maeng.game.domain.jwac.service.JwacService;
import com.maeng.game.domain.lobby.service.LobbyService;
import com.maeng.game.domain.room.dto.CreateRoomDTO;
import com.maeng.game.domain.room.dto.EnterDTO;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.dto.KickDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.dto.PlayerDTO;
import com.maeng.game.domain.room.dto.RoomInfoDTO;
import com.maeng.game.domain.room.dto.RoomStateDTO;
import com.maeng.game.domain.room.dto.SeatDTO;
import com.maeng.game.domain.room.dto.SeatInfoDTO;
import com.maeng.game.domain.room.dto.UserInfo;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.entity.Seat;
import com.maeng.game.domain.room.entity.User;
import com.maeng.game.domain.room.repository.RoomRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final AwrspService awrspService;
    private final JwacService jwacService;
    private final GsbService gsbService;
    private final LobbyService lobbyService;
    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "room";
    @Value("${game.max}")
    private int GAME_MAX_PLAYER;
    @Value("${game.gsb.min}")
    private int GSB_MIN_PLAYER;
    @Value("${game.jwac.min}")
    private int JWAC_MIN_PLAYER;
    @Value("${game.awrsp.min}")
    private int AWRSP_MIN_PLAYER;
    @Value("${game.gsb.max}")
    private int GSB_MAX_PLAYER;
    @Value("${game.awrsp.max}")
    private int AWRSP_MAX_PLAYER;
    @Value("${game.jwac.max}")
    private int JWAC_MAX_PLAYER;

    @Transactional
    @Operation(summary = "대기방 생성")
    public synchronized String createRoom(CreateRoomDTO createRoomDTO){

        String roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        Room room = Room.builder()
                .id(roomCode)
                .title(createRoomDTO.getTitle())
                .createdAt(LocalDateTime.now())
                .headCount(0)
                .maxHeadCount(this.getMaxPlayer(createRoomDTO.getGameCategory()))
                .minHeadCount(this.getMinPlayer(createRoomDTO.getGameCategory()))
                .publicRoom(createRoomDTO.isPublicRoom())
                .participant(null)
                .gameCategory(createRoomDTO.getGameCategory())
                .channelTire(createRoomDTO.getChannelTire())
                .seats(this.seatInit(createRoomDTO.getGameCategory()))
                .gameCode("")
                .gameStart(false)
                .build();

        roomRepository.save(room);
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST

        return roomCode;
    }

    @Transactional
    @Operation(summary = "대기방 입장")
    public synchronized void enterRoom(String roomCode, EnterDTO enterDTO){
        Room roomInfo = this.getCurrentRoom(roomCode);


        if(roomInfo.getHeadCount() == GAME_MAX_PLAYER){
            throw new PullRoomException("플레이어가 가득 찬 방입니다.");
        }

        UserInfo userInfo = getUserInfo(enterDTO.getNickname());

        if(!roomInfo.getChannelTire().equals(userInfo.getTier()) && !roomInfo.getChannelTire().equals(ChannelTire.UNRANKED)){
            throw new NotEqualsTier("해당 채널과 티어가 달라 입장할 수 없습니다.");
        }

        // 방이 가득차지 않았으면 headCount++ 후 User 추가해주기
        int headCount = roomInfo.getHeadCount();
        User user = User.builder()
                .nickname(enterDTO.getNickname())
                .ready(false)
                .host(true)
                .profileUrl(userInfo.getProfile())
                .tier(userInfo.getTier())
                .build();

        HashMap<Integer, Seat> seats = roomInfo.getSeats();
        HashMap<String, User> newList = new HashMap<>();

        int seat = findEmptySeat(roomInfo.getSeats());
        seats.put(seat, Seat.builder()
                                    .available(true)
                                    .nickname(enterDTO.getNickname())
                                    .build());

        if(roomInfo.getParticipant() != null) { // 방장이 아닐 경우
            newList.putAll(roomInfo.getParticipant());
            user.setHost(false);
        }
        newList.put(user.getNickname(), user);

        roomInfo.setParticipant(newList);
        roomInfo.setHeadCount(headCount+1);
        roomInfo.setSeats(seats);
        roomRepository.save(roomInfo);

        this.sendRoomInfo(roomCode, roomInfo); // ROOM_INFO
        lobbyService.findAllRoom(roomInfo.getGameCategory(), roomInfo.getChannelTire()); // ROOM_LIST

    }

    @Transactional
    @Operation(summary = "게임 시작")
    public void gameStart(String roomCode, PlayerDTO playerDTO){
        Room room = getCurrentRoom(roomCode);

        // 해당 닉네임이 host이면 게임 시작
        User user = room.getParticipant().get(playerDTO.getNickname());
        if(!user.isHost()){
            throw new NotHostException("방장만 게임을 시작할 수 있습니다.");
        }

        // 방장도 ready 상태로 변경
        // this.readyPlayer(roomCode, PlayerDTO.builder().nickname(user.getNickname()).build());
        room = this.getCurrentRoom(roomCode);
        this.checkCount(room.getGameCategory(), room.getHeadCount()); // 최소 인원 확인
        this.checkReady(room.getParticipant()); // 플레이어 레디 상태 확인
        this.start(room, roomCode); // 게임시작
    }

    @Transactional
    @Operation(summary = "플레이어 레디")
    public synchronized Room readyPlayer(String roomCode, PlayerDTO readyDTO){

        // 방 정보에서 해당 플레이어 레디 상태 바꾸고
        Room room = getCurrentRoom(roomCode);
        HashMap<String, User> players = room.getParticipant();
        User user = players.get(readyDTO.getNickname());

        user.setReady(!user.isReady());
        players.put(user.getNickname(), user);
        room.setParticipant(players);
        roomRepository.save(room);

        return room;
    }

    @Transactional
    @Operation(summary = "대기방 퇴장")
    public synchronized void exitRoom(String roomCode, PlayerDTO exitDTO){
        Room room = getCurrentRoom(roomCode);

        if(room.isGameStart()){
            return;
        }

        HashMap<String, User> players = room.getParticipant();
        HashMap<Integer, Seat> seats = room.getSeats();
        User user = players.get(exitDTO.getNickname());

        if(!players.containsKey(exitDTO.getNickname())){ // 이미 나감처리 된 유저이면 리턴
            return;
        }

        players.remove(exitDTO.getNickname());
        room.setParticipant(players);
        room.setHeadCount(room.getHeadCount()-1);

        if(room.getHeadCount() == 0){ // 방에 아무도 남아있지 않다면 방 정보 삭제
            roomRepository.delete(room);
            lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
            log.info("roomCode : " +roomCode+ " 방 삭제 완");
            return;
        }

        int seatNumber = -1;
        for(Integer i : seats.keySet()){
            if(seats.get(i).getNickname().equals(exitDTO.getNickname())){
                seatNumber = i;
            }
        }

        // 해당 자리 초기화
        seats.put(seatNumber, Seat.builder().available(true).nickname("").build());
        room.setSeats(seats);

        // 방장 위임
        List<String> temp = new ArrayList<>(room.getParticipant().keySet());
        if(user.isHost()){ // 나간 사람이 방장이면 남은 사람 중 한 명을 방장으로
            User nextHost = players.get(temp.get(0));
            nextHost.setHost(true);
        }
        roomRepository.save(room);

        // ROOM_EXIT
        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_EXIT")
                .data(PlayerDTO.builder().nickname(exitDTO.getNickname()).build())
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);

        this.sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
    }

    @Transactional
    @Operation(summary = "자리 상태 변경(자리 열기/닫기)")
    public void seatStateChange(String roomCode, SeatDTO seatDTO){

        Room room = this.getCurrentRoom(roomCode);
        checkHost(room, seatDTO.getNickname()); // 방장 권한 확인

        // 자리 상태, MaxHeadCount 변경
        Seat seat = room.getSeats().get(seatDTO.getSeatNumber());


        if(this.checkSeat(room, seat)){
            // 최소 인원 이상 닫을 수 없게 & 최대 인원 이상 열 수 없게
            return;
        }

        room.setMaxHeadCount(seat.isAvailable() ? room.getMaxHeadCount() - 1 : room.getMaxHeadCount() + 1);
        seat.setAvailable(!seat.isAvailable()); // 자리 상태 변경
        room.getSeats().put(seatDTO.getSeatNumber(), seat);
        roomRepository.save(room);

        sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
    }

    @Transactional
    @Operation(summary = "플레이어 강퇴")
    public void kickPlayer(String roomCode, KickDTO kickDTO){
        Room room = this.getCurrentRoom(roomCode);
        checkHost(room, kickDTO.getNickname()); // 방장 권한 확인

        HashMap<String, User> players = room.getParticipant();
        HashMap<Integer, Seat> seats = room.getSeats();

        // 해당 플레이어 삭제, HeadCount, 자리 사용가능으로 변경
        int seatNumber = 0;
        for(Integer i : seats.keySet()){
            if(seats.get(i).getNickname().equals(kickDTO.getKickPlayer())){
                seatNumber = i;
            }
        }

        players.remove(kickDTO.getKickPlayer());
        seats.put(seatNumber, Seat.builder().available(true).nickname("").build());
        room.setParticipant(players);
        room.setSeats(seats);
        room.setHeadCount(room.getHeadCount()-1);
        roomRepository.save(room);

        // 강퇴된 사람 닉네임 보내주기
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, MessageDTO.builder()
                .type("PLAYER_KICK").data(PlayerDTO.builder().nickname(kickDTO.getNickname())).build());
        this.sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire());
    }

    @Operation(summary = "대기방 설정 변경")
    public void roomStateChange(String roomCode, RoomStateDTO roomStateDTO){
        Room room = this.getCurrentRoom(roomCode);
        checkHost(room, roomStateDTO.getNickname());
        room.setTitle(roomStateDTO.getTitle());
        room.setPublicRoom(roomStateDTO.isPublicRoom());
        roomRepository.save(room);

        this.sendRoomInfo(roomCode, room);
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire());
    }


    @Operation(summary = "게임 중 소켓 연결 끊긴 유저 처리")
    public Room disconnectPlayer(String gameCode, String nickname){
        List<Room> room = roomRepository.findAllByGameCode(gameCode);

        if(room == null){
            log.info("방없음 ?!@?#!?@?!?!@?");
        }

        return room.get(0);
    }

    public void start(Room room, String roomCode){
        String gameCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        List<User> users = new ArrayList<>(room.getParticipant().values());
        GameStartDTO gameStartDTO = GameStartDTO.builder()
                .roomCode(roomCode)
                .headCount(room.getHeadCount())
                .participant(users)
                .gameCode(gameCode)
                .build();

        boolean settingCheck = false;

        // 각 gameService의 start 호출
        if(room.getGameCategory().equals(Game.ALL_WIN_ROCK_SCISSOR_PAPER)){
            settingCheck = awrspService.gameSetting(gameStartDTO);
        }

        if(room.getGameCategory().equals(Game.GOLD_SILVER_BRONZE)){
            settingCheck = gsbService.gameSetting(gameStartDTO);
        }

        if(room.getGameCategory().equals(Game.JEWELRY_AUCTION)){
            settingCheck = jwacService.gameSetting(gameStartDTO);
        }

        if(!settingCheck){
            throw new GameSettingException("게임 정보 초기화 실패");
        }

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, MessageDTO.builder()
                .type("GAME_START").data(gameCode).build());

        // 게임 시작하면 비공개 방으로 변경
        room.setPublicRoom(false);
        room.setGameCode(gameCode);
        room.setGameStart(true);
        roomRepository.save(room);

        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire());
    }

    public void checkCount(Game game, int headCount){
        // 게임 시작 최소 인원 확인
        int max = 0;

        if(game.equals(Game.GOLD_SILVER_BRONZE)){
            max = GSB_MIN_PLAYER;
        }

        if(game.equals(Game.JEWELRY_AUCTION)){
            max = JWAC_MIN_PLAYER;
        }

        if(game.equals(Game.ALL_WIN_ROCK_SCISSOR_PAPER)){
            max = AWRSP_MIN_PLAYER;
        }

        if(headCount < max){
            throw new MinHeadCountException("플레이어가 부족합니다. 현재 플레이어 수 : "+headCount);
        }
    }

    public void checkReady(HashMap<String, User> participant){
        // 모든 플레이어 ready 확인
        List<User> users = new ArrayList<>(participant.values());

        for(User user : users){
            if(user == null){
                continue;
            }

            if(user.isHost()){
                continue;
            }

            if(!user.isReady()){
                log.info(user.getNickname());
                throw new NotReadyPlayerException("모든 플레이어가 준비되어야 합니다.");
            }
        }
    }

    public void enterNotice(String roomCode, EnterDTO enterDTO){
        EnterDTO chatDTO = EnterDTO.builder()
                .nickname(enterDTO.getNickname())
                .roomCode(roomCode)
                .build();

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_ENTER")
                .data(chatDTO)
                .build();

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);
    }


    @Operation(summary = "방 정보 전송")
    public void sendRoomInfo(String roomCode, Room roomInfo){

        SeatInfoDTO[] users = new SeatInfoDTO[GAME_MAX_PLAYER];
        HashMap<Integer, Seat> list = roomInfo.getSeats();

        // TODO : 열려있는지도 저장
        for(int i = 0; i < GAME_MAX_PLAYER; i++){

            User user = null;

            if(!list.get(i).getNickname().equals("")){
                user = roomInfo.getParticipant().get(list.get(i).getNickname());
            }

            users[i] = SeatInfoDTO.builder()
                    .open(list.get(i).isAvailable())
                    .user(user)
                    .build();
        }

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_INFO")
                .data(RoomInfoDTO.builder()
                        .title(roomInfo.getTitle())
                        .roomCode(roomCode)
                        .gameCategory(roomInfo.getGameCategory())
                        .participant(users)
                        .headCount(roomInfo.getHeadCount())
                        .maxHeadCount(roomInfo.getMaxHeadCount())
                        .publicRoom(roomInfo.isPublicRoom())
                        .build())
                .build();

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);
    }

    public HashMap<Integer, Seat> seatInit(Game gameCategory){
        HashMap<Integer, Seat> seats = new HashMap<>();
        boolean seat = true;

        for(int i = 0; i < 8; i++){
            if(Game.GOLD_SILVER_BRONZE.equals(gameCategory) && i == 2){
                seat = false;
            }

            seats.put(i, Seat.builder()
                            .available(seat)
                            .nickname("")
                            .build());
        }

        return seats;
    }

    public Room getCurrentRoom(String roomCode){
        Room room = roomRepository.findById(roomCode).orElse(null);

        if(room == null){
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        return room;
    }

    public void checkHost(Room room, String nickname){
        User user = room.getParticipant().get(nickname);

        if(!user.isHost()){
            throw new NotHostException("권한이 없습니다.");
        }
    }

    public int findEmptySeat(HashMap<Integer, Seat> users){

        for(Integer i : users.keySet()){
            if(users.get(i).getNickname().equals("")){
                log.info("빈 자리 : "+i);
                return i;
            }
        }

        return -1;
    }

    public boolean checkSeat(Room room, Seat seat){
        HashMap<Integer, Seat> seats = room.getSeats();

        int count = 0;
        for(Seat s : seats.values()){
            if(s.isAvailable()){
                count++;
            }
        }

        return (seat.isAvailable() && (count == room.getHeadCount())) ||
                (!seat.isAvailable() && (count == room.getHeadCount()));
    }
    public int getMaxPlayer(Game gameCategory){
        if(gameCategory.equals(Game.ALL_WIN_ROCK_SCISSOR_PAPER)){
            return AWRSP_MAX_PLAYER;
        }

        if(gameCategory.equals(Game.JEWELRY_AUCTION)){
            return JWAC_MAX_PLAYER;
        }

        if(gameCategory.equals(Game.GOLD_SILVER_BRONZE)){
            return GSB_MAX_PLAYER;
        }

        return GAME_MAX_PLAYER;
    }

    public int getMinPlayer(Game gameCategory){
        if(gameCategory.equals(Game.ALL_WIN_ROCK_SCISSOR_PAPER)){
            return AWRSP_MIN_PLAYER;
        }

        if(gameCategory.equals(Game.JEWELRY_AUCTION)){
            return JWAC_MIN_PLAYER;
        }

        if(gameCategory.equals(Game.GOLD_SILVER_BRONZE)){
            return GSB_MIN_PLAYER;
        }

        return 2;
    }

    public UserInfo getUserInfo(String nickname) {
        try {
            String baseUrl = "http://maengland.com:9120/api/v1/user/profile";
            String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
            String requestUrl = baseUrl + "/" + encodedNickname;

            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // HTTP 응답 본문을 문자열로 읽어 반환
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();

                    return objectMapper.readValue(response.toString(), UserInfo.class);
                }
            } else {
                // 이미지 다운로드 실패 시 에러 메시지 반환
                log.info(nickname + " 프로필 사진 가져오기 실패" + responseCode);

                return UserInfo.builder().build();
            }
        } catch (IOException e) {
            // 예외 처리
            log.info(nickname + " 프로필 사진 가져오기 실패");
            return UserInfo.builder().build();
        }
    }
}
