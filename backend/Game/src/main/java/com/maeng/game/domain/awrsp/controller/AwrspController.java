package com.maeng.game.domain.awrsp.controller;

import com.maeng.game.domain.awrsp.dto.SubmitDTO;
import com.maeng.game.domain.awrsp.dto.TimerDTO;
import com.maeng.game.domain.awrsp.entity.Card;
import com.maeng.game.domain.awrsp.exception.TimerTypeException;
import com.maeng.game.domain.awrsp.service.AwrspService;
import com.maeng.game.domain.awrsp.service.AwrspTimerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AwrspController {
    private final AwrspService awrspService;
    private final AwrspTimerService awrspTimerService;
    @Operation(summary = "[테스트용] 정답 카드 생성")
    @MessageMapping("awrsp.create")
    public void start(){
        Card[] card = awrspService.generateCard();
        log.info(Arrays.toString(card));
    }

    @Operation(summary = "타이머 종료")
    @MessageMapping("awrsp.timer.{gameCode}")
    public void timerEnd(@DestinationVariable String gameCode, TimerDTO timerDTO){
        boolean finish = awrspTimerService.timerEnd(gameCode, timerDTO);
        if(finish){
            this.callGameMethod(gameCode, timerDTO.getType());
            awrspTimerService.timerStart(gameCode, timerDTO.getType()); // 그 다음 타이머 호출
        }
    }

    @Operation(summary = "카드 제출")
    @MessageMapping("awrsp.submit.{gameCode}")
    public void cardSubmit(@DestinationVariable String gameCode, SubmitDTO submitDTO){
        boolean finish = awrspService.submitCard(gameCode, submitDTO);
        if(finish){
            awrspTimerService.timerStart(gameCode, "CARD_SUBMIT"); // 그 다음 타이머 호출
        }
    }

    @Operation(summary = "타입에 따른 게임 로직 함수 호출")
    public void callGameMethod(String gameCode, String type){
        if(type.equals("ENTER_GAME")){ // 게임 참가
            awrspService.gameStart(gameCode);
            return;
        }

        if(type.equals("CARD_OPEN")){ // 정답 카드 공개 끝났을 때

            return;
        }

        if(type.equals("CARD_SUBMIT")){ // 카드 제출 끝났을 때
            return;
        }

        if(type.equals("PLAYER_WINS")){ // 본인의 승 수 공개 끝났을 때
            return;
        }

        if(type.equals("ALL_WINS")){ // 모든 플레이어의 승 수 공개 끝났을 때
            return;
        }

        throw new TimerTypeException("잘못된 타이머 타입입니다.");
    }
}
