package com.maeng.game.domain.room.service;

import com.maeng.game.domain.awrsp.game.service.AwrspService;
import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.lobby.service.LobbyService;
import com.maeng.game.domain.room.dto.*;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Player;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.entity.Seat;
import com.maeng.game.domain.room.exception.*;
import com.maeng.game.domain.room.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final AwrspService awrspService;
    private final LobbyService lobbyService;
    private final RabbitTemplate template;
    private final static String CHAT_EXCHANGE_NAME = "room";
    @Value("${game.max}")
    private final int GAME_MAX_PLAYER = 8;
    @Value("${game.gsb-min}")
    private int GSB_MIN_PLAYER;
    @Value("${game.jwac-min}")
    private int JWAC_MIN_PLAYER;
    @Value("${game.awrsp-min}")
    private int AWRSP_MIN_PLAYER;

    @Operation(summary = "대기방 생성")
    public String createRoom(CreateRoomDTO createRoomDTO){

        String roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        HashMap<Integer, Seat> seats = this.seatInit();

        Room room = Room.builder()
                .id(roomCode)
                .title(createRoomDTO.getTitle())
                .createdAt(LocalDateTime.now())
                .headCount(0)
                .maxHeadCount(GAME_MAX_PLAYER)
                .publicRoom(createRoomDTO.isPublicRoom())
                .participant(null)
                .gameCategory(createRoomDTO.getGameCategory())
                .channelTire(createRoomDTO.getChannelTire())
                .seats(seats)
                .build();

        roomRepository.save(room);

        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST

        return roomCode;
    }

    @Operation(summary = "대기방 입장")
    public void enterRoom(String roomCode, EnterDTO enterDTO){
        Room roomInfo = this.getCurrentRoom(roomCode);

        if(roomInfo.getHeadCount() == GAME_MAX_PLAYER){
            throw new PullRoomException("플레이어가 가득 찬 방입니다.");
        }

        // 방이 가득차지 않았으면 headCount++ 후 Player 추가해주기
        int headCount = roomInfo.getHeadCount();
        Player player = Player.builder()
                .nickname(enterDTO.getNickname())
                .ready(false)
                .host(true)
                .profileUrl("넣어야 됨")
                .tier(Tier.GOLD) // TODO : 플레이어 정보 가져오기(프로필 사진, 티어)
                .build();

        HashMap<Integer, Seat> seats = roomInfo.getSeats();
        HashMap<String, Player> newList = new HashMap<>();

        seats.put(headCount+1, Seat.builder()
                                    .available(false)
                                    .nickname(enterDTO.getNickname())
                                    .build());

        if(roomInfo.getParticipant() != null) { // 방장이 아닐 경우
            newList.putAll(roomInfo.getParticipant());
            player.setHost(false);
        }

        newList.put(player.getNickname(), player);
        roomInfo.setParticipant(newList);
        roomInfo.setHeadCount(headCount+1);
        roomInfo.setSeats(seats);
        roomRepository.save(roomInfo);

        this.sendRoomInfo(roomCode, roomInfo); // ROOM_INFO
        lobbyService.findAllRoom(roomInfo.getGameCategory(), roomInfo.getChannelTire()); // ROOM_LIST
    }

    @Operation(summary = "게임 시작")
    public void gameStart(String roomCode, PlayerDTO playerDTO){
        Room room = getCurrentRoom(roomCode);

        // 해당 닉네임이 host이면 게임 시작
        Player player = room.getParticipant().get(playerDTO.getNickname());
        if(!player.isHost()){
            throw new NotHostException("방장만 게임을 시작할 수 있습니다.");
        }

        this.checkCount(room.getGameCategory(), room.getHeadCount()); // 최소 인원 확인
        this.checkReady(room.getParticipant()); // 플레이어 레디 상태 확인
        this.start(room, roomCode); // 게임시작
    }

    @Operation(summary = "플레이어 레디")
    public Room readyPlayer(String roomCode, ReadyDTO readyDTO){

        // 방 정보에서 해당 플레이어 레디 상태 바꾸고
        Room room = getCurrentRoom(roomCode);
        HashMap<String, Player> players = room.getParticipant();
        Player player = players.get(readyDTO.getNickname());

        player.setReady(readyDTO.isReady());
        players.put(player.getNickname(), player);
        room.setParticipant(players);
        roomRepository.save(room);

        return room;
    }

    @Operation(summary = "대기방 퇴장")
    public void exitRoom(String roomCode, ExitDTO exitDTO){
        Room room = getCurrentRoom(roomCode);

        HashMap<String, Player> players = room.getParticipant();
        HashMap<Integer, Seat> seats = room.getSeats();
        Player player = players.get(exitDTO.getNickname());

        players.remove(exitDTO.getNickname());
        room.setParticipant(players);
        room.setHeadCount(room.getHeadCount()-1);

        if(room.getHeadCount() == 0){ // 방에 아무도 남아있지 않다면 방 정보 삭제
            roomRepository.delete(room);
            lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
            log.info("roomCode : " +roomCode+ " 방 삭제 완");
            return;
        }

        // 해당 자리 초기화
        seats.put(exitDTO.getSeatNumber(), Seat.builder().available(true).nickname(null).build());

        // 방장 위임
        List<String> temp = new ArrayList<>(room.getParticipant().keySet());
        if(player.isHost()){ // 나간 사람이 방장이면 남은 사람 중 한 명을 방장으로
            Player nextHost = players.get(temp.get(0));
            nextHost.setHost(true);
        }
        roomRepository.save(room);

        // ROOM_EXIT
        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_EXIT")
                .data(PlayerDTO.builder().roomCode(roomCode).nickname(exitDTO.getNickname()).build())
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);

        this.sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
    }

    @Operation(summary = "자리 상태 변경(자리 열기/닫기)")
    public void seatStateChange(String roomCode, SeatDTO seatDTO){
        Room room = this.getCurrentRoom(roomCode);
        checkHost(room, seatDTO.getNickname()); // 방장 권한 확인

        // 자리 상태, MaxHeadCount 변경
        Seat seat = room.getSeats().get(seatDTO.getSeatNumber());
        room.setMaxHeadCount(seat.isAvailable() ? room.getMaxHeadCount() - 1 : room.getMaxHeadCount() + 1);
        seat.setAvailable(!seat.isAvailable());
        room.getSeats().put(seatDTO.getSeatNumber(), seat);
        roomRepository.save(room);

        sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire()); // ROOM_LIST
    }

    @Operation(summary = "플레이어 강퇴")
    public void kickPlayer(String roomCode, KickDTO kickDTO){
        Room room = this.getCurrentRoom(roomCode);
        checkHost(room, kickDTO.getNickname()); // 방장 권한 확인

        HashMap<String, Player> players = room.getParticipant();
        HashMap<Integer, Seat> seats = room.getSeats();

        // 해당 플레이어 삭제, HeadCount, 자리 사용가능으로 변경
        players.remove(kickDTO.getKickPlayer());
        seats.put(kickDTO.getSeatNumber(), Seat.builder().available(true).nickname(null).build());
        room.setParticipant(players);
        room.setSeats(seats);
        room.setHeadCount(room.getHeadCount()-1);
        roomRepository.save(room);

        this.sendRoomInfo(roomCode, room); // ROOM_INFO
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire());
    }

    @Operation(summary = "대기방 설정 변경")
    public void roomStateChange(String roomCode, RoomStateDTO roomStateDTO){
        Room room = this.getCurrentRoom(roomCode);
        room.setTitle(roomStateDTO.getTitle());
        room.setPublicRoom(roomStateDTO.isPublicRoom());
        roomRepository.save(room);

        this.sendRoomInfo(roomCode, room);
        lobbyService.findAllRoom(room.getGameCategory(), room.getChannelTire());
    }

    public void start(Room room, String roomCode){
        List<Player> players = new ArrayList<>(room.getParticipant().values());
        GameStartDTO gameStartDTO = GameStartDTO.builder()
                .roomCode(roomCode)
                .headCount(room.getHeadCount())
                .participant(players)
                .build();

        // 각 gameService의 start 호출
        if(room.getGameCategory().equals(Game.ALL_WIN_ROCK_SCISSOR_PAPER)){
            awrspService.gameStart(gameStartDTO);
        }

        if(room.getGameCategory().equals(Game.GOLD_SILVER_BRONZE)){
            awrspService.gameStart(gameStartDTO);
        }

        if(room.getGameCategory().equals(Game.JEWELRY_AUCTION)){
            awrspService.gameStart(gameStartDTO);
        }
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

    public void checkReady(HashMap<String, Player> participant){
        // 모든 플레이어 ready 확인
        List<Player> players = new ArrayList<>(participant.values());

        for(Player player : players){
            if(!player.isReady()){
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

        List<Player> players = new ArrayList<>(roomInfo.getParticipant().values());

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_INFO")
                .data(RoomInfoDTO.builder()
                        .title(roomInfo.getTitle())
                        .roomCode(roomCode)
                        .gameCategory(roomInfo.getGameCategory())
                        .participant(players)
                        .headCount(roomInfo.getHeadCount())
                        .maxHeadCount(roomInfo.getMaxHeadCount())
                        .publicRoom(roomInfo.isPublicRoom())
                        .build())
                .build();

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);
    }

    public HashMap<Integer, Seat> seatInit(){
        HashMap<Integer, Seat> seats = new HashMap<>();

        for(int i = 1; i <= 8; i++){
            seats.put(i, Seat.builder()
                            .available(true)
                            .nickname(null)
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
        Player player = room.getParticipant().get(nickname);

        if(!player.isHost()){
            throw new NotHostException("권한이 없습니다.");
        }

    }
}
