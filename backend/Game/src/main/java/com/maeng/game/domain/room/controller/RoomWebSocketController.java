package com.maeng.game.domain.room.controller;

import com.maeng.game.domain.awrsp.game.service.AwrspService;
import com.maeng.game.domain.room.dto.*;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Player;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.exception.MinHeadCountException;
import com.maeng.game.domain.room.exception.NotFoundRoomException;
import com.maeng.game.domain.room.exception.NotReadyPlayerException;
import com.maeng.game.domain.room.repository.RoomRepository;
import com.maeng.game.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomWebSocketController {

    private final RabbitTemplate template;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final AwrspService awrspService;
    private final static String CHAT_EXCHANGE_NAME = "room.exchange";
    private final static String CHAT_QUEUE_NAME = "room.queue";
    @Value("${game.gsb-min}")
    private int GSB_MIN_PLAYER;
    @Value("${game.jwac-min}")
    private int JWAC_MIN_PLAYER;
    @Value("${game.awrsp-min}")
    private int AWRSP_MIN_PLAYER;


    // TODO : 대기방 입장
    @MessageMapping("room.enter.{roomCode}")
    public void enter(@DestinationVariable("roomCode") String roomCode, EnterDTO enterDTO){

        ChatDTO chatDTO = ChatDTO.builder()
                .nickname(enterDTO.getNickname())
                .message(enterDTO.getNickname()+"님이 입장하셨습니다.")
                .build();

        MessageDTO messageDTO = MessageDTO.builder()
                                .type("ROOM_ENTER")
                                .data(chatDTO)
                                .build();

        Room roomInfo = roomService.enterRoom(roomCode, enterDTO.getNickname()); // player 추가
        template.convertAndSend(CHAT_EXCHANGE_NAME, "enter.room."+roomCode, messageDTO);

        // 방에 있는 모든 사람에게 갱신된 ROOM_INFO 전송
//        messageDTO = MessageDTO.builder()
//                    .type("ROOM_INFO")
//                    .data(RoomInfoDTO.builder()
//                            .title(roomInfo.getTitle())
//                            .createdAt(roomInfo.getCreatedAt())
//                            .gameCategory(roomInfo.getGameCategory())
//                            .participant(roomInfo.getParticipant())
//                            .headCount(roomInfo.getHeadCount())
//                            .maxHeadCount(roomInfo.getMaxHeadCount())
//                            .publicRoom(roomInfo.isPublicRoom())
//                            .build())
//                    .build();
//
//        template.convertAndSend(CHAT_EXCHANGE_NAME, "enter.room."+roomCode, messageDTO);
    }

    // TODO : 대기실 채팅
    @MessageMapping("room.message.{roomCode}")
    public void send(@DestinationVariable("roomCode") String roomCode, ChatDTO chatDTO){

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_CHAT")
                .data(chatDTO)
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "*.room."+roomCode, messageDTO);
    }

    // TODO : 게임 시작
    @MessageMapping("room.start.{roomCode}")
    public void ready(@DestinationVariable("roomCode") String roomCode){
        // TODO : 모든 플레이어 ready 확인
        Room room = roomRepository.findById(roomCode).orElse(null);

        if(room == null){
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        checkCount(room.getGameCategory(), room.getHeadCount()); // 최소 인원 확인
        checkReady(room.getParticipant()); // 플레이어 레디 상태 확인
        gameStart(room, roomCode); // 게임시작
    }

    public void gameStart(Room room, String roomCode){
        GameStartDTO gameStartDTO = GameStartDTO.builder()
                .roomCode(roomCode)
                .headCount(room.getHeadCount())
                .participant(room.getParticipant())
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
    public void checkReady(List<Player> participant){
        // 모든 플레이어 ready 확인
        for(Player player : participant){
            if(!player.isReady()){
                throw new NotReadyPlayerException("모든 플레이어가 준비되어야 합니다.");
            }
        }
    }
}
