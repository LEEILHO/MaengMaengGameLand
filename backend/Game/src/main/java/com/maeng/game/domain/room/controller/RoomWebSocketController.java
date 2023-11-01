package com.maeng.game.domain.room.controller;

import com.maeng.game.domain.room.dto.*;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomWebSocketController {

    private final RabbitTemplate template;
    private final RoomService roomService;
    private final static String CHAT_EXCHANGE_NAME = "room.exchange";

    @Operation(summary = "대기방 입장")
    @MessageMapping("room.enter.{roomCode}")
    public void enter(@DestinationVariable("roomCode") String roomCode, EnterDTO enterDTO){
        roomService.enterNotice(roomCode, enterDTO); // 입장 알림
        roomService.enterRoom(roomCode, enterDTO.getNickname()); // player 추가
    }

    @Operation(summary = "대기방 채팅")
    @MessageMapping("room.message.{roomCode}")
    public void send(@DestinationVariable("roomCode") String roomCode, ChatDTO chatDTO){

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_CHAT")
                .data(chatDTO)
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomCode, messageDTO);
    }

    @Operation(summary = "플레이어 레디")
    @MessageMapping("room.ready.{roomCode}")
    public void ready(@DestinationVariable String roomCode, ReadyDTO readyDTO){
        Room room = roomService.readyPlayer(roomCode, readyDTO);
        roomService.sendRoomInfo(roomCode, room);
    }

    @Operation(summary = "게임 시작")
    @MessageMapping("room.start.{roomCode}")
    public void start(@DestinationVariable("roomCode") String roomCode, PlayerDTO playerDTO){
        roomService.gameStart(roomCode, playerDTO);
    }

    @Operation(summary = "대기방 퇴장")
    @MessageMapping("room.exit.{roomCode}")
    public void exit(@DestinationVariable String roomCode, PlayerDTO playerDTO){
        roomService.exitRoom(roomCode, playerDTO);
    }
}
