package com.maeng.game.domain.awrsp.controller;

import com.maeng.game.domain.awrsp.entity.Card;
import com.maeng.game.domain.awrsp.service.AwrspService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AwrspController {
    private final AwrspService awrspService;
    @Operation(summary = "[테스트용] 정답 카드 생성")
    @MessageMapping("game.awrsp.create")
    public void start(){
        Card[] card = awrspService.generateCard();
        log.info(Arrays.toString(card));
    }

    @Operation(summary = "참여 확인")
    @MessageMapping("awrsp.enter.{gameCode}")
    public void enterGame(@DestinationVariable String gameCode){
        awrspService.enterGame(gameCode);
    }

    @Operation(summary = "정답 카드 공개")
    @MessageMapping("awrsp.card.{gameCode}")
    public void game(@DestinationVariable String gameCode){

    }
}
