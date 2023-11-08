package com.maeng.game.domain.gsb.controller;

import com.maeng.game.domain.gsb.dto.BettingDto;
import com.maeng.game.domain.gsb.dto.GsbNicknameDto;
import com.maeng.game.domain.gsb.dto.PlayerSeqDto;
import com.maeng.game.domain.gsb.dto.StarDto;
import com.maeng.game.domain.gsb.entity.Gsb;
import com.maeng.game.domain.gsb.entity.StartCard;
import com.maeng.game.domain.gsb.service.GsbEnterService;
import com.maeng.game.domain.gsb.service.GsbService;
import com.maeng.game.domain.room.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class GsbController {

    private final RabbitTemplate template;

    private final GsbService gsbService;
    private final GsbEnterService enterService;
    private final static String Game_EXCHANGE_NAME = "game";


    @MessageMapping("game.gsb.enter.{gameCode}")
    public void enter(@DestinationVariable String gameCode, GsbNicknameDto nicknameDto){

        int headCount = 2;
        // 2명이 들어왔다면
        if(enterService.enter(gameCode,headCount,nicknameDto)){
            // 카드 전송
            // 숫자 카드 랜덤 전송
            StartCard [] cards = gsbService.getStartCards(gameCode);
            template.convertAndSend(Game_EXCHANGE_NAME,"gsb."+gameCode, MessageDTO.builder()
                    .type("플레이어순서")
                    .data(cards)
                    .build());

        }

    }

    @MessageMapping("game.gsb.set-player.{gameCode}")
    public void setPlayer(@DestinationVariable String gameCode, PlayerSeqDto playerSeqDto){
        gsbService.setSeq(gameCode,playerSeqDto);
        StartCard [] cards = gsbService.getInfo(gameCode).getStartCards();
        template.convertAndSend(Game_EXCHANGE_NAME,"gsb."+gameCode, MessageDTO.builder()
                .type("플레이어순서")
                .data(cards)
                .build());

        // 모든 플레이어 세팅 완료
        if(gsbService.getInfo(gameCode).getPlayers() != null && gsbService.getInfo(gameCode).getPlayers().size()==2){
            Gsb gsb = gsbService.setGsb(gameCode);

            template.convertAndSend(Game_EXCHANGE_NAME,"gsb."+gameCode, MessageDTO.builder()
                    .type("게임정보")
                    .data(gsb)
                    .build());
        }


    }


    @MessageMapping("game.gsb.set-star.{gameCode}")
    public void setStar(@DestinationVariable String gameCode, StarDto starDto) {

        template.convertAndSend(Game_EXCHANGE_NAME, "gsb."+gameCode,gsbService.setStar( gameCode , starDto ));


    }
    @MessageMapping("game.gsb.betting.{gameCode}")
    public void setBet(@DestinationVariable String gameCode, BettingDto bettingDto){
        /*TODO: 베팅 DTO 반환 */

        template.convertAndSend(Game_EXCHANGE_NAME,"gsb."+gameCode,gsbService.setBet(gameCode,bettingDto));



        /*TODO: 라운드가 종료 되었으면 라운드 결과 반환 + 다음플레이어 해야할 일 반환*/

        if(gsbService.endRound(gameCode)){
            template.convertAndSend(Game_EXCHANGE_NAME,"gsb."+gameCode,gsbService.getRoundResult(gameCode));


        }


    }
    @GetMapping
    public ResponseEntity<?> test(){


        return ResponseEntity.ok().body(gsbService.getInfo("1234"));
    }








}
