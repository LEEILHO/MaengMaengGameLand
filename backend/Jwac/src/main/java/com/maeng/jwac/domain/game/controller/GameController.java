package com.maeng.jwac.domain.game.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.jwac.domain.game.dto.PlayerInfo;
import com.maeng.jwac.domain.game.emums.Tier;
import com.maeng.jwac.domain.game.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class GameController {
	private final GameService gameService;

	@PostMapping("/generate")
	public void test() {
		List<PlayerInfo> playerInfo = new ArrayList<>();
		playerInfo.add(PlayerInfo.builder().nickname("1111").profileUrl("test/11").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().nickname("2222").profileUrl("test/22").tier(Tier.GOLD).build());
		playerInfo.add(PlayerInfo.builder().nickname("3333").profileUrl("test/33").tier(Tier.BRONZE).build());
		playerInfo.add(PlayerInfo.builder().nickname("4444").profileUrl("test/44").tier(Tier.CHALLENGER).build());
		playerInfo.add(PlayerInfo.builder().nickname("5555").profileUrl("test/55").tier(Tier.SILVER).build());
		playerInfo.add(PlayerInfo.builder().nickname("6666").profileUrl("test/66").tier(Tier.BRONZE).build());
		gameService.generateGame("abcdefg", "abcdefg123456", playerInfo);
	}
}
