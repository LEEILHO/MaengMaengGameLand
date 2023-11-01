package com.maeng.game.domain.jwac.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.dto.JwacNicknameDto;
import com.maeng.game.domain.jwac.entity.Enter;
import com.maeng.game.domain.jwac.repository.EnterRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnterService {
	private final EnterRedisRepository enterRedisRepository;

	@Transactional
	public boolean enter(String gameCode, int headCount, JwacNicknameDto jwacNicknameDto) {
		Enter enter = enterRedisRepository.findById(gameCode)
			.orElse(Enter.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

		enter.getNicknames().add(jwacNicknameDto.getNickname());

		boolean allEnter = (enter.getNicknames().size() == headCount);

		if(allEnter) {
			enter.getNicknames().clear();
		}

		enterRedisRepository.save(enter);

		return allEnter;
	}
}