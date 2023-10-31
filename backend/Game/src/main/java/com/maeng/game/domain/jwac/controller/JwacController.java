package com.maeng.game.domain.jwac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.jwac.service.EnterService;
import com.maeng.game.domain.jwac.service.JwacService;
import com.maeng.game.domain.jwac.service.TimerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwac")
public class JwacController {
	private final JwacService jwacService;
	private final TimerService timerService;
	private final EnterService enterService;

	@PostMapping("/enter/{gameCode}")
	public void enter(@PathVariable String gameCode, @RequestBody JwacNicknameDto jwacNicknameDto) {
		// TODO :  방 정보에서 인원수 가져오기
		int headCount = 6;
		if(enterService.enter(gameCode, headCount, jwacNicknameDto)) {
			// TODO : 방 정보에서 사용자 정보 가져오기
			jwacService.generateGame("abcdefg", null);
			timerService.timerStart();
		}
	}

	@PostMapping("/generate")
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

	@PostMapping("/bid/{gameCode}")
	public void bid(@RequestBody JwacBidInfoDto jwacBidInfoDto) {
		jwacService.bidJwerly(jwacBidInfoDto);
	}

	@PostMapping("/time/{gameCode}")
	public ResponseEntity<JwacRoundResultDto> end(@PathVariable String gameCode, @RequestBody JwacNicknameDto jwacNicknameDto) {
		// 현재 라운드 결과 확인
		JwacRoundResultDto jwacRoundResult = null;
		int headCount = jwacService.getHeadCount(gameCode);
		if(timerService.timerEnd(gameCode, headCount, jwacNicknameDto)) {
			jwacRoundResult = jwacService.endRound(gameCode);

			// 다음 라운드
			if(jwacService.nextRound(gameCode)) {
				// TODO : 새로운 타이머 시작
				timerService.timerStart();
			}
		}

		// 결과 전송
		if(jwacRoundResult != null) {
			return ResponseEntity.ok(jwacRoundResult);
		} else {
			return ResponseEntity.status(5000).build();
		}
	}
}
