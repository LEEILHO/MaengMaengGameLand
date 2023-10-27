package com.maeng.game.domain.jwac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.jwac.service.JwacService;
import com.maeng.game.domain.jwac.service.TimerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwac")
public class JwacController {
	private final JwacService jwacService;
	private final TimerService timerService;

	@PostMapping("/generate")
	public void test() {
		List<PlayerInfo> playerInfo = new ArrayList<>();
		playerInfo.add(PlayerInfo.builder().userEmail("1111").profileUrl("test/11").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().userEmail("2222").profileUrl("test/22").tier(Tier.GOLD).build());
		playerInfo.add(PlayerInfo.builder().userEmail("3333").profileUrl("test/33").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().userEmail("4444").profileUrl("test/44").tier(Tier.CHALLENGER).build());
		playerInfo.add(PlayerInfo.builder().userEmail("5555").profileUrl("test/55").tier(Tier.SILVER).build());
		playerInfo.add(PlayerInfo.builder().userEmail("6666").profileUrl("test/66").tier(Tier.BRONZE).build());
		jwacService.generateGame("abcdefg", playerInfo);
	}

	@PostMapping("/bid/{gameCode}")
	public void bid(@RequestBody JwacBidInfoDto jwacBidInfoDto) {
		jwacService.bidJwerly(jwacBidInfoDto);
	}

	@PostMapping("/time/{gameCode}")
	public ResponseEntity<JwacRoundResultDto> end(@RequestParam String gameCode, String nickname, int round) {
		boolean timerEnd = timerService.timerEnd(gameCode, nickname, round);

		JwacRoundResultDto jwacRoundResult = null;
		if(timerEnd) {
			jwacRoundResult = jwacService.nextRound(gameCode);
		}

		// TODO : 새로운 타이머 시작
		timerService.timerStart();

		if(jwacRoundResult != null) {
			return ResponseEntity.ok(jwacRoundResult);
		} else {
			return ResponseEntity.status(5000).build();
		}
	}
}
