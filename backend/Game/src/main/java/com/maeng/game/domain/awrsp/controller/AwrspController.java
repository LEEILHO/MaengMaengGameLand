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
            String type = this.callGameMethod(gameCode, timerDTO.getType());
            awrspTimerService.timerStart(gameCode, type); // 그 다음 타이머 호출
        }
    }

    @Operation(summary = "카드 제출")
    @MessageMapping("awrsp.submit.{gameCode}")
    public void cardSubmit(@DestinationVariable String gameCode, SubmitDTO submitDTO){
        boolean finish = awrspService.submitCard(gameCode, submitDTO);
        if(finish){
            awrspService.getWinCount(gameCode);
            if(awrspService.checkGameOver(gameCode)){
                awrspTimerService.timerStart(gameCode, "PLAYER_WINS"); // 그 다음 타이머 호출
            }else{
                awrspService.sendGameResult(gameCode);
            }
        }
    }

    @Operation(summary = "타입에 따른 게임 로직 함수 호출")
    public String callGameMethod(String gameCode, String type){
        if(type.equals("ENTER_GAME")){ // 게임 참가
            awrspService.enterGame(gameCode); // 라운드 초기화

            if(awrspService.passDrawCard(gameCode)){ // 1~2라운드이면 카드제출로 바로 넘어가기
                return "DRAW_CARD";
            }
            return type;
        }


        if(type.equals("CARD_SUBMIT")){ // 정답 카드 공개 끝났을 때
            awrspService.getWinCount(gameCode);
            return type;
        }

        throw new TimerTypeException("잘못된 타이머 타입입니다.");
    }
}
