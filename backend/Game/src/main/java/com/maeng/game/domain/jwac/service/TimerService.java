package com.maeng.game.domain.jwac.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.repository.TimerRedisRepository;
import com.maeng.game.domain.jwac.entity.Timer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimerService {
	private final JwacService jwacService;

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

		int headCount = jwacService.getHeadCount(gameCode);

		if(timer.getNicknames().size() == headCount) {
			jwacService.nextRound(gameCode);
			timer.getNicknames().clear();
		}

		timerRedisRepository.save(timer);
	}
}
