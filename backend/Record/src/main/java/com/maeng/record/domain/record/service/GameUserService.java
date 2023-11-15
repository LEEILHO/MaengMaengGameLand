package com.maeng.record.domain.record.service;

import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.dto.NicknameEditDTO;
import com.maeng.record.domain.record.dto.UserInfoDTO;
import com.maeng.record.domain.record.entity.GameUser;
import com.maeng.record.domain.record.repository.GameUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameUserService {
	private final GameUserRepository gameUserRepository;

	public void registerGameUser(UserInfoDTO userinfo) {
		if(gameUserRepository.existsByEmail(userinfo.getEmail())) {
			return;
		}

		gameUserRepository.save(GameUser.builder()
			.email(userinfo.getEmail())
			.nickname(userinfo.getNickname())
			.build());
	}

	public void editGameUser(NicknameEditDTO nicknameEditDTO) {
		if(nicknameEditDTO.getEmail() == null || nicknameEditDTO.getNickname() == null) {
			return;
		}
		GameUser gameUser = gameUserRepository.findByEmail(nicknameEditDTO.getEmail()).orElseThrow();
		gameUser.setNickname(nicknameEditDTO.getNickname());
		gameUserRepository.save(gameUser);
	}
}
