package com.maeng.game.domain.jwac.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {
	private final TimerRedisRepository timerRedisRepository;

	@Value("${game.jwac.round.time}")
	private int ROUND_TIME;

	@Transactional
	public synchronized boolean timerEnd(String gameCode, Map<String, Player> players, JwacNicknameDto jwacNicknameDto) {
		log.info("jwac timerEnd: {}", gameCode);
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		Set<String> nicknames = timer.getNicknames();
		if(nicknames == null) {
			nicknames = new HashSet<>();
		}
		if(players.containsKey(jwacNicknameDto.getNickname())) {
			Objects.requireNonNull(nicknames).add(jwacNicknameDto.getNickname());
		}

		boolean timerEnd = (Objects.requireNonNull(nicknames).size() == getHeadCount(players));
		if(timerEnd) {
			nicknames.clear();
		}

		timer.setNicknames(nicknames);
		timerRedisRepository.save(timer);

		return timerEnd;
	}

	public int checkTimer(String gameCode, Map<String, Player> players) {
		log.info("jwac checkTimer: {}", gameCode);
		Timer timer = timerRedisRepository.findById(gameCode)
			.orElse(Timer.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		Set<String> nicknames = timer.getNicknames();
		if(nicknames == null) {
			nicknames = new HashSet<>();
		}

		long headCount = getHeadCount(players);
		boolean timerEnd = (Objects.requireNonNull(nicknames).size() == headCount);
		if(timerEnd) {
			nicknames.clear();
		}

		timer.setNicknames(nicknames);
		timerRedisRepository.save(timer);

		log.info("jwac checkTimer: {}, headCount: {}", timerEnd, headCount);
		return timerEnd ? 1 : (headCount == 0 ? 0 : -1);
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
