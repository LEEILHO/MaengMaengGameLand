package com.maeng.game.domain.jwac.controller;

import java.util.List;
import java.util.Map;

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
import com.maeng.game.domain.jwac.entity.Player;
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

	private static final String GAME_EXCHANGE_NAME = "game";
	private static final String RECORD_EXCHANGE_NAME = "record";

	@Value("${game.jwac.round.special}")
	private int SPECIAL_ROUND;

	private final RabbitTemplate template;
	private final JwacService jwacService;
	private final TimerService timerService;
	private final EnterService enterService;

	@MessageMapping("game.jwac.enter.{gameCode}")
	public void enter(@DestinationVariable String gameCode, JwacNicknameDto jwacNicknameDto) {
		log.info("enter -- " + "gameCode: " + gameCode + ", nickname: " + jwacNicknameDto.getNickname());
		Map<String, Player> players = jwacService.getPlayers(gameCode);
		if(enterService.enter(gameCode, players, jwacNicknameDto)) {
			sendGameInfoMessage(gameCode);
			startGameRound(gameCode);
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
		Map<String, Player> players = jwacService.getPlayers(gameCode);

		if(timerService.timerEnd(gameCode, players, jwacNicknameDto)) {
			handleRoundResult(gameCode);
		}
	}

	public void disconnectPlayer(String gameCode, String nickname) {
		Map<String, Player> players = jwacService.disconnectPlayer(gameCode, nickname);
		if(timerService.checkTimer(gameCode, players)) {
			handleRoundResult(gameCode);
		}
	}

	private void sendGameInfoMessage(String gameCode) {
		List<PlayerInfo> players = jwacService.getGamePlayer(gameCode);
		template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
			.type("GAME_INFO")
			.data(JwacGameInfoDTO.builder()
				.gameCode(gameCode)
				.playerInfo(players)
				.build())
			.build());
	}

	private void startGameRound(String gameCode) {
		Jewelry nextJewelry = jwacService.gameStart(gameCode);
		JwacTimerInfoDTO timerInfo = timerService.timerStart(gameCode);
		timerInfo.setRound(1);
		timerInfo.setJewelry(nextJewelry);
		timerInfo.setJewelryScore(nextJewelry.getIndex());

		template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
			.type("GAME_ROUND_START")
			.data(timerInfo)
			.build());
	}

	private void handleRoundResult(String gameCode) {
		JwacRoundResultDto jwacRoundResult = jwacService.endRound(gameCode);

		template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
			.type("GAME_ROUND_RESULT")
			.data(jwacRoundResult)
			.build());

		handleNextRoundOrGameEnd(gameCode, jwacRoundResult);
	}

	private void handleNextRoundOrGameEnd(String gameCode, JwacRoundResultDto jwacRoundResult) {
		Jewelry nextJewelry = jwacService.nextRound(gameCode);

		if (nextJewelry != null) {
			startNextRound(gameCode, jwacRoundResult, nextJewelry);
		} else {
			endGame(gameCode);
		}
	}

	private void startNextRound(String gameCode, JwacRoundResultDto jwacRoundResult, Jewelry nextJewelry) {
		JwacTimerInfoDTO timerInfo = timerService.timerStart(gameCode);
		timerInfo.setRound(jwacRoundResult.getRound() + 1);
		timerInfo.setJewelry(nextJewelry);
		timerInfo.setJewelryScore(nextJewelry.getIndex());

		template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
			.type("GAME_ROUND_START")
			.data(timerInfo)
			.build());

		handleSpecialItemResult(gameCode, jwacRoundResult);
	}

	private void handleSpecialItemResult(String gameCode, JwacRoundResultDto jwacRoundResult) {
		if (jwacRoundResult.getRound() == SPECIAL_ROUND) {
			JwacItemResultDTO itemResult = jwacService.getSpecialItemResult(gameCode, jwacRoundResult.getMostBidder());

			template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
				.type("GAME_SPECIAL_ITEM_RESULT")
				.data(itemResult)
				.build());
		}
	}

	private void endGame(String gameCode) {
		JwacGameResultDTO gameResult = jwacService.endGame(gameCode);

		template.convertAndSend(GAME_EXCHANGE_NAME, "jwac." + gameCode, MessageDTO.builder()
			.type("GAME_END")
			.data(gameResult)
			.build());

		// record 서버에 결과 저장 요청
		template.convertAndSend(RECORD_EXCHANGE_NAME, "jwac." + gameCode, jwacService.getAllDataToJson(gameCode));
	}
}
