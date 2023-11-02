package com.maeng.game.domain.awrsp.game.controller;

import com.maeng.game.domain.awrsp.game.entity.Card;
import com.maeng.game.domain.awrsp.game.service.AwrspService;
import com.maeng.game.domain.room.dto.ChatDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AwrspController {
    private final AwrspService awrspService;
    @MessageMapping("game.awrsp.create")
    public void start(){
        Card[] card = awrspService.generateCard();
        log.info(Arrays.toString(card));
    }
}
