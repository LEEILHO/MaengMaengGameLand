package com.maeng.jwac.domain.timer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		if(timer.getNicknames() == null) {
			timer.setNicknames(new HashSet<>());
		}

		timer.getNicknames().add(nickname);

		System.out.println(timer.toString());

		int headCount = gameService.getHeadCount(gameCode);

		if(timer.getNicknames().size() == headCount) {
			gameService.nextRound(gameCode);
			timer.getNicknames().clear();
		}

		timerRedisRepository.save(timer);
	}
}
