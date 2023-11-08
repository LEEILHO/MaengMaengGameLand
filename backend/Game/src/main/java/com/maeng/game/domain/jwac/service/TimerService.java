package com.maeng.game.domain.jwac.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.dto.JwacTimerInfoDTO;
import com.maeng.game.domain.jwac.entity.Timer;
import com.maeng.game.domain.jwac.repository.TimerRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimerService {
	private final TimerRedisRepository timerRedisRepository;

	private final static int ROUND_TIME = 20;

	@Transactional
	public boolean timerEnd(String gameCode, int headCount, JwacNicknameDto jwacNicknameDto) {
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		if(timer.getNicknames() == null) {
			timer.setNicknames(new HashSet<>());
		}

		timer.getNicknames().add(jwacNicknameDto.getNickname());

		boolean timerEnd = (timer.getNicknames().size() == headCount);
		if(timerEnd) {
			timer.getNicknames().clear();
		}

		timerRedisRepository.save(timer);

		return timerEnd;
	}

	@Transactional
	public JwacTimerInfoDTO timerStart(String gameCode) {
		return JwacTimerInfoDTO.builder()
			.gameCode(gameCode)
			.time(ROUND_TIME)
			.build();
	}

	public void timerCreate(String gameCode) {
		timerRedisRepository.save(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());
	}
}
