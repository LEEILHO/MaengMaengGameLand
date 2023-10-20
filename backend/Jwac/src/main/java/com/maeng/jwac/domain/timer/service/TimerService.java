package com.maeng.jwac.domain.timer.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.jwac.domain.game.service.GameService;
import com.maeng.jwac.domain.timer.entity.Timer;
import com.maeng.jwac.domain.timer.repository.TimerRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimerService {
	private final GameService gameService;

	private final TimerRedisRepository timerRedisRepository;

	@Transactional
	public void timerEnd(String gameCode, String nickname, int round) {
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashMap<>()).build());

		timer.getNicknames().put(round, nickname);

		int headCount = gameService.getHeadCount(gameCode);

		timerRedisRepository.save(timer);

		if(timer.getNicknames().get(round).length() == headCount) {
			gameService.nextRound(gameCode);
		}
	}
}
