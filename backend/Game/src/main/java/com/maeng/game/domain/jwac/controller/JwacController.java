package com.maeng.game.domain.jwac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacGameResultDTO;
import com.maeng.game.domain.jwac.dto.JwacItemResultDTO;
import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.JwacTimerInfoDTO;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Jwerly;
import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.jwac.service.EnterService;
import com.maeng.game.domain.jwac.service.JwacService;
import com.maeng.game.domain.jwac.service.TimerService;
import com.maeng.game.domain.room.dto.MessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class JwacController {
	private final RabbitTemplate template;

	private final JwacService jwacService;
	private final TimerService timerService;
	private final EnterService enterService;

	private final static String Game_EXCHANGE_NAME = "game";
	private final static String RECORD_EXCHANGE_NAME = "record";

	@MessageMapping("game.jwac.{gameCode}")
	public void enter(@DestinationVariable String gameCode, JwacNicknameDto jwacNicknameDto) {
		// TODO :  방 정보에서 인원수 가져오기
		int headCount = 6;
		if(enterService.enter(gameCode, headCount, jwacNicknameDto)) {
			// TODO : 방 정보에서 사용자 정보 가져오기
			test();
			// jwacService.generateGame("abcdefg", null);
			timerService.timerStart(gameCode);
		}
	}

	@PostMapping("/test")
	public void test() {
		List<PlayerInfo> playerInfo = new ArrayList<>();
		playerInfo.add(PlayerInfo.builder().nickname("1111").profileUrl("test/11").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().nickname("2222").profileUrl("test/22").tier(Tier.GOLD).build());
		playerInfo.add(PlayerInfo.builder().nickname("3333").profileUrl("test/33").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().nickname("4444").profileUrl("test/44").tier(Tier.CHALLENGER).build());
		playerInfo.add(PlayerInfo.builder().nickname("5555").profileUrl("test/55").tier(Tier.SILVER).build());
		playerInfo.add(PlayerInfo.builder().nickname("6666").profileUrl("test/66").tier(Tier.BRONZE).build());
		jwacService.generateGame("abcdefg", playerInfo);
	}

	@MessageMapping("game.jwac.bid.{gameCode}")
	public void bid(@DestinationVariable String gameCode, JwacBidInfoDto jwacBidInfoDto) {
		log.info("bid");
		jwacService.bidJwerly(gameCode, jwacBidInfoDto);
	}

	@MessageMapping("game.jwac.time.{gameCode}")
	public void timerEnd(@DestinationVariable String gameCode, JwacNicknameDto jwacNicknameDto) {
		log.info("timerEnd");
		// 현재 라운드 결과 확인
		int headCount = jwacService.getHeadCount(gameCode);
		if(timerService.timerEnd(gameCode, headCount, jwacNicknameDto)) {
			JwacRoundResultDto jwacRoundResult = jwacService.endRound(gameCode);

			template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
				.type("GAME_ROUND_RESULT")
				.data(jwacRoundResult)
				.build());

			// 다음 라운드
			Jwerly nextJwerly = jwacService.nextRound(gameCode);
			if(nextJwerly != null) {
				JwacTimerInfoDTO timerInfo = timerService.timerStart(gameCode);
				timerInfo.setRound(jwacRoundResult.getRound() + 1);
				timerInfo.setJwerly(nextJwerly);
				timerInfo.setJwerlyScore(nextJwerly.getIndex());

				template.convertAndSend(Game_EXCHANGE_NAME, "jwac."+gameCode, MessageDTO.builder()
					.type("GAME_ROUND_START")
					.data(timerInfo)
					.build());

				// 10 라운드에서 아이템 결과 전송
				log.info("round : " + jwacRoundResult.getRound());
				if(jwacRoundResult.getRound() == 10) {
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
