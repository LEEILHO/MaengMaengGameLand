package com.maeng.game.domain.jwac.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.entity.Enter;
import com.maeng.game.domain.jwac.entity.Player;
import com.maeng.game.domain.jwac.repository.EnterRedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterService {
	private final EnterRedisRepository enterRedisRepository;

	@Transactional
	public synchronized boolean enter(String gameCode, Map<String, Player> players, JwacNicknameDto jwacNicknameDto) {
		Enter enter = enterRedisRepository.findById(gameCode)
			.orElse(Enter.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		Set<String> nicknames = enter.getNicknames();
		if(nicknames == null) {
			log.info("enter.getNicknames() is null");
			enter.setNicknames(new HashSet<>());
		}

		if(players.containsKey(jwacNicknameDto.getNickname())) {
			nicknames.add(jwacNicknameDto.getNickname());
			log.info(enter.toString());
		}

		boolean allEnter = (nicknames.size() >= getHeadCount(players));

		if(allEnter) {
			enter.getNicknames().clear();
			enterRedisRepository.delete(enter);
			log.info("delete(enter): {}", gameCode);
		} else {
			enterRedisRepository.save(enter);
			log.info("save(enter): {}", gameCode);
		}

		return allEnter;
	}

	private long getHeadCount(Map<String, Player> players) {
		return players.values().stream()
			.filter(Player::isInGame)
			.count();
	}

}
