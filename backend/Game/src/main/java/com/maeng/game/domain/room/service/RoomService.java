package com.maeng.game.domain.room.service;

import com.maeng.game.domain.awrsp.game.service.AwrspService;
import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.room.dto.*;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Player;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.exception.*;
import com.maeng.game.domain.room.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final AwrspService awrspService;
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

    // TODO : 대기방 생성
    public String createRoom(CreateRoomDTO createRoomDTO){

        String roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        roomRepository.save(Room.builder()
                        .id(roomCode)
                        .title(createRoomDTO.getTitle())
                        .createdAt(createRoomDTO.getCreatedAt())
                        .headCount(0)
                        .maxHeadCount(GAME_MAX_PLAYER)
                        .publicRoom(createRoomDTO.isPublicRoom())
                        .participant(null)
                        .gameCategory(createRoomDTO.getGameCategory())
                        .channelTire(createRoomDTO.getChannelTire())
                .build());

        return roomCode;
    }

    // 대기방 입장
    public void enterRoom(String roomCode, String nickname){

        // 방에 사람이 가득찼는지 확인하기
        Room roomInfo = roomRepository.findById(roomCode).orElse(null);
        if(roomInfo == null){
            log.info(roomCode);
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        if(roomInfo.getHeadCount() == GAME_MAX_PLAYER){
            throw new PullRoomException("플레이어가 가득 찬 방입니다.");
        }

        // 방이 가득차지 않았으면 headCount++ 후 Player 추가해주기
        int headCount = roomInfo.getHeadCount();
        //List<Player> newList = new ArrayList<>();
        Player player = Player.builder()
                .nickname(nickname)
                .ready(false)
                .host(true)
                .profileUrl("넣어야 됨")
                .tier(Tier.GOLD) // TODO : 플레이어 정보 가져오기(프로필 사진, 티어)
                .build();

        HashMap<String, Player> newList = new HashMap<>();
        if(roomInfo.getParticipant() != null) { // 방장이 아닐 경우
            newList.putAll(roomInfo.getParticipant());
            player.setHost(false);
        }

        newList.put(player.getNickname(), player);

        roomInfo.setParticipant(newList);
        roomInfo.setHeadCount(headCount+1);

        roomRepository.save(roomInfo);

        sendRoomInfo(roomCode, roomInfo);

    }

    public void gameStart(String roomCode, PlayerDTO playerDTO){
        Room room = roomRepository.findById(roomCode).orElse(null);

        if(room == null){
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        // TODO : 해당 닉네임이 host이면 게임 시작
        Player player = room.getParticipant().get(playerDTO.getNickname());
        if(!player.isHost()){
            throw new NotHostException("방장만 게임을 시작할 수 있습니다.");
        }

        checkCount(room.getGameCategory(), room.getHeadCount()); // 최소 인원 확인
        checkReady(room.getParticipant()); // 플레이어 레디 상태 확인
        start(room, roomCode); // 게임시작
    }

    public Room readyPlayer(String roomCode, ReadyDTO readyDTO){

        // TODO : 방 정보에서 해당 플레이어 레디 상태 바꾸고
        Room room = roomRepository.findById(roomCode).orElse(null);

        if(room == null){
            throw new NotFoundRoomException("방이 존재하지 않습니다.");
        }

        HashMap<String, Player> players = room.getParticipant();
        Player player = players.get(readyDTO.getNickname());
        log.info(player.getNickname());

        player.setReady(readyDTO.isReady());
        players.put(player.getNickname(), player);
        room.setParticipant(players);
        roomRepository.save(room);

        return room;
    }

    @Operation(summary = "대기방 퇴장")
    public void exitRoom(String roomCode, PlayerDTO playerDTO){
        Room room = roomRepository.findById(roomCode).orElse(null);
        if(room == null){
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        HashMap<String, Player> players = room.getParticipant();
        Player player = players.get(playerDTO.getNickname());

        players.remove(playerDTO.getNickname());
        room.setParticipant(players);
        room.setHeadCount(room.getHeadCount()-1);

        if(room.getHeadCount() == 0){ // 방에 아무도 남아있지 않다면 방 정보 삭제
            roomRepository.delete(room);
            log.info("roomCode : " +roomCode+ "방 삭제 완");
            return;
        }

        List<String> temp = new ArrayList<>(room.getParticipant().keySet());
        if(player.isHost()){ // 나간 사람이 방장이면 남은 사람 중 한 명을 방장으로
            Player nextHost = players.get(temp.get(0));
            nextHost.setHost(true);
        }

        roomRepository.save(room);
        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_EXIT")
                .data(PlayerDTO.builder().roomCode(roomCode).nickname(playerDTO.getNickname()).build())
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);

        this.sendRoomInfo(roomCode, room);
    }

    public void start(Room room, String roomCode){
        GameStartDTO gameStartDTO = GameStartDTO.builder()
                .roomCode(roomCode)
                .headCount(room.getHeadCount())
                //.participant(room.getParticipant())
                .build();

        // TODO : 각 gameService의 start 호출
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

    // TODO : sendMessageToUser - 특정 사용자에게 메세지 보낼 때

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

}
