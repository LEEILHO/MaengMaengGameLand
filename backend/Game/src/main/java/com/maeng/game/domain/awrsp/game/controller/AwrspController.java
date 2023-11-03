package com.maeng.game.domain.awrsp.game.controller;

import com.maeng.game.domain.room.dto.ChatDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class AwrspController {
    private final RabbitTemplate template;
    private final static String GAME_EXCHANGE_NAME = "game.exchange";

    @MessageMapping("game.awrsp.{roomCode}")
    public void start(@DestinationVariable("roomCode") String roomCode, ChatDTO chatDTO){

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_CHAT")
                .data(chatDTO)
                .build();
        template.convertAndSend(GAME_EXCHANGE_NAME, "*.game.awrsp."+roomCode, messageDTO);
    }

}
