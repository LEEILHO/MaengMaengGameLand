package com.maeng.user.domain.score;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.user.domain.score.dto.GiveScoreDTO;
import com.maeng.user.domain.score.service.ScoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/score")
public class controller {
	private final ScoreService scoreService;
	@PostMapping("/give")
	public void giveScore(@RequestBody GiveScoreDTO giveScoreDTO) {
		scoreService.giveUserScoreAdmin(giveScoreDTO);
	}
}
