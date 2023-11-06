package com.maeng.game.domain.gsb.controller;

import com.maeng.game.domain.gsb.dto.GsbNicknameDto;
import com.maeng.game.domain.gsb.dto.PlayerSeqDto;
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
                    .type("PlayerSeq")
                    .data(cards)
                    .build());

        }

    }

    @MessageMapping("game.gsb.set-player.{gameCode}")
    public void setPlayer(@DestinationVariable String gameCode, PlayerSeqDto playerSeqDto){

        gsbService.setSeq(gameCode,playerSeqDto);

    }
    @GetMapping
    public ResponseEntity<?> test(){


        return ResponseEntity.ok().body(gsbService.getInfo("1234"));
    }








}
