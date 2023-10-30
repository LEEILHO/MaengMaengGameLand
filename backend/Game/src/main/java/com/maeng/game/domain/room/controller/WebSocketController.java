package com.maeng.game.domain.room.controller;

import com.maeng.game.domain.room.dto.ChatDTO;
import com.maeng.game.domain.room.dto.EnterDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.dto.RoomInfoDTO;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RabbitTemplate template;
    private final RoomService roomService;
    private final static String CHAT_EXCHANGE_NAME = "room.exchange";
    private final static String CHAT_QUEUE_NAME = "room.queue";


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

        Room roomInfo = roomService.enterRoom(roomCode, enterDTO.getNickname());
        template.convertAndSend(CHAT_EXCHANGE_NAME, "enter.room."+roomCode, messageDTO);

        // 방에 있는 모든 사람에게 갱신된 ROOM_INFO 전송
//        messageDTO = MessageDTO.builder()
//                    .type("ROOM_INFO")
//                    .data(RoomInfoDTO.builder()
//                            .title(roomInfo.getTitle())
//                            .createdAt(roomInfo.getCreatedAt())
//                            .maxHeadCount(roomInfo.getMaxHeadCount())
//                            .gameCategory(roomInfo.getGameCategory())
//                            .participant(roomInfo.getParticipant())
//                            .participant(roomInfo.getParticipant())
//                            .headCount(roomInfo.getHeadCount())
//                            .publicRoom(roomInfo.isPublicRoom())
//                            .build())
//                    .build();

        log.info(messageDTO.toString());

        template.convertAndSend(CHAT_EXCHANGE_NAME, "*.room."+roomCode, messageDTO);
    }

    @MessageMapping("room.message.{roomCode}")
    public void send(@DestinationVariable("roomCode") String roomCode, ChatDTO chatDTO){

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_CHAT")
                .data(chatDTO)
                .build();
        template.convertAndSend(CHAT_EXCHANGE_NAME, "*.room."+roomCode, messageDTO);
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(MessageDTO messageDTO){
        log.info("Message : "+ messageDTO.getData().toString());
    }

}
