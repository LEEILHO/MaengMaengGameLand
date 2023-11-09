package com.maeng.game.domain.jwac.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacGameInfoDTO;
import com.maeng.game.domain.jwac.dto.JwacGameResultDTO;
import com.maeng.game.domain.jwac.dto.JwacItemResultDTO;
import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.JwacTimerInfoDTO;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Jewelry;
import com.maeng.game.domain.jwac.service.EnterService;
import com.maeng.game.domain.jwac.service.JwacService;
import com.maeng.game.domain.jwac.service.TimerService;
import com.maeng.game.domain.room.dto.MessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JwacController {

	@Value("${game.jwac.round.special}")
	private int SPECIAL_ROUND;


	private final RabbitTemplate template;

	private final JwacService jwacService;
	private final TimerService timerService;
	private final EnterService enterService;

	private final static String Game_EXCHANGE_NAME = "game";
	private final static String RECORD_EXCHANGE_NAME = "record";

	@MessageMapping("game.jwac.enter.{gameCode}")
	public void enter(@DestinationVariable String gameCode, JwacNicknameDto jwacNicknameDto) {
		log.info("enter -- " + "gameCode: " + gameCode + ", nickname: " + jwacNicknameDto.getNickname());
		int headCount = jwacService.getHeadCount(gameCode);
		if(enterService.enter(gameCode, headCount, jwacNicknameDto)) {
			List<PlayerInfo> players = jwacService.getGamePlayer(gameCode);
			template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
				.type("GAME_INFO")
				.data(JwacGameInfoDTO.builder()
					.gameCode(gameCode)
					.playerInfo(players)
					.build())
				.build());

			Jewelry nextJewelry = jwacService.gameStart(gameCode);
			JwacTimerInfoDTO timerInfo = timerService.timerStart(gameCode);
			timerInfo.setRound(1);
			timerInfo.setJewelry(nextJewelry);
			timerInfo.setJewelryScore(nextJewelry.getIndex());

			template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
				.type("GAME_ROUND_START")
				.data(timerInfo)
				.build());
		}
	}


	@MessageMapping("game.jwac.bid.{gameCode}")
	public void bid(@DestinationVariable String gameCode, JwacBidInfoDto jwacBidInfoDto) {
		log.info("bid -- " + "gameCode: " + gameCode + ", nickname: " + jwacBidInfoDto.getNickname()
			+ ", round: " + jwacBidInfoDto.getRound() + ", bidAmount: " + jwacBidInfoDto.getBidAmount());
		jwacService.bidJewelry(gameCode, jwacBidInfoDto);
	}


	@MessageMapping("game.jwac.time.{gameCode}")
	public void timerEnd(@DestinationVariable String gameCode, JwacNicknameDto jwacNicknameDto) {
		log.info("time -- " + "gameCode: " + gameCode + ", nickname: " + jwacNicknameDto.getNickname());
		// 현재 라운드 결과 확인
		int headCount = jwacService.getHeadCount(gameCode);
		if(timerService.timerEnd(gameCode, headCount, jwacNicknameDto)) {
			JwacRoundResultDto jwacRoundResult = jwacService.endRound(gameCode);

			template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
				.type("GAME_ROUND_RESULT")
				.data(jwacRoundResult)
				.build());

			// 다음 라운드
			Jewelry nextJewelry = jwacService.nextRound(gameCode);
			if(nextJewelry != null) {
				JwacTimerInfoDTO timerInfo = timerService.timerStart(gameCode);
				timerInfo.setRound(jwacRoundResult.getRound() + 1);
				timerInfo.setJewelry(nextJewelry);
				timerInfo.setJewelryScore(nextJewelry.getIndex());

				template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
					.type("GAME_ROUND_START")
					.data(timerInfo)
					.build());

				// 10 라운드에서 아이템 결과 전송
				log.info("round : " + jwacRoundResult.getRound());
				if(jwacRoundResult.getRound() == SPECIAL_ROUND) {
					JwacItemResultDTO itemResult = jwacService.getSpecialItemResult(gameCode, jwacRoundResult.getMostBidder());

					template.convertAndSend(Game_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
						.type("GAME_SPECIAL_ITEM_RESULT")
						.data(itemResult)
						.build());
				}

			// 게임 종료
			} else {
				JwacGameResultDTO gameResult = jwacService.endGame(gameCode);

				template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
					.type("GAME_END")
					.data(gameResult)
					.build());

				// record 서버에 결과 저장 요청
				template.convertAndSend(RECORD_EXCHANGE_NAME, "jwac."+gameCode, jwacService.getAllDataToJson(gameCode));
			}
		}
	}
}
