package com.maeng.game.domain.jwac.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.dto.JwacTimerInfoDTO;
import com.maeng.game.domain.jwac.entity.Player;
import com.maeng.game.domain.jwac.entity.Timer;
import com.maeng.game.domain.jwac.repository.TimerRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimerService {
	private final TimerRedisRepository timerRedisRepository;

	@Value("${game.jwac.round.time}")
	private int ROUND_TIME;

	@Transactional
	public synchronized boolean timerEnd(String gameCode, Map<String, Player> players, JwacNicknameDto jwacNicknameDto) {
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		Set<String> nicknames = timer.getNicknames();
		if(nicknames == null) {
			timer.setNicknames(new HashSet<>());
		}

		if(players.containsKey(jwacNicknameDto.getNickname())) {
			nicknames.add(jwacNicknameDto.getNickname());
		}

		boolean timerEnd = (nicknames.size() == getHeadCount(players));
		if(timerEnd) {
			nicknames.clear();
		}

		timerRedisRepository.save(timer);

		return timerEnd;
	}

	public boolean checkTimer(String gameCode, Map<String, Player> players) {
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		Set<String> nicknames = timer.getNicknames();
		if(nicknames == null) {
			timer.setNicknames(new HashSet<>());
		}

		boolean timerEnd = (nicknames.size() == getHeadCount(players));
		if(timerEnd) {
			nicknames.clear();
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

	private long getHeadCount(Map<String, Player> players) {
		return players.values().stream()
			.filter(Player::isInGame)
			.count();
	}

}
