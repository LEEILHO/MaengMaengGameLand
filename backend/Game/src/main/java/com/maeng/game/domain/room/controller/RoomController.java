package com.maeng.game.domain.room.controller;

import com.maeng.game.domain.room.dto.*;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RabbitTemplate template;
    private final RoomService roomService;
    private final static String CHAT_EXCHANGE_NAME = "room.exchange";


    @MessageMapping("room.create")
    public void create(CreateRoomDTO createRoomDTO){
        String roomCode = roomService.createRoom(createRoomDTO);
        roomService.enterRoom(roomCode, createRoomDTO.getHost());
        log.info(roomCode);
    }

    // TODO : 대기방 입장
    @MessageMapping("room.enter.{roomCode}")
    public void enter(@DestinationVariable("roomCode") String roomCode, EnterDTO enterDTO){

        EnterDTO chatDTO = EnterDTO.builder()
                .nickname(enterDTO.getNickname())
                .roomCode(roomCode)
                .build();

        MessageDTO messageDTO = MessageDTO.builder()
                                .type("ROOM_ENTER")
                                .data(chatDTO)
                                .build();

        Room roomInfo = roomService.enterRoom(roomCode, enterDTO.getNickname()); // player 추가
        template.convertAndSend(CHAT_EXCHANGE_NAME, "enter.room."+roomCode, messageDTO);

        // 방에 있는 모든 사람에게 갱신된 ROOM_INFO 전송
        messageDTO = MessageDTO.builder()
                    .type("ROOM_INFO")
                    .data(RoomInfoDTO.builder()
                            .title(roomInfo.getTitle())
                            .roomCode(roomCode)
                            .gameCategory(roomInfo.getGameCategory())
                            .participant(roomInfo.getParticipant())
                            .headCount(roomInfo.getHeadCount())
                            .maxHeadCount(roomInfo.getMaxHeadCount())
                            .publicRoom(roomInfo.isPublicRoom())
                            .build())
                    .build();

        template.convertAndSend(CHAT_EXCHANGE_NAME, "enter.room."+roomCode, messageDTO);
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
        roomService.gameStart(roomCode);
    }

}
