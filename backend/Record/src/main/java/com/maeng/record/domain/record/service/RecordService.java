package com.maeng.record.domain.record.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.dto.GameParticipantDTO;
import com.maeng.record.domain.record.dto.UserGameInfoDTO;
import com.maeng.record.domain.record.entity.GameParticipant;
import com.maeng.record.domain.record.entity.GameUser;
import com.maeng.record.domain.record.repository.GameParticipantRepository;
import com.maeng.record.domain.record.repository.GameRepository;
import com.maeng.record.domain.record.repository.GameUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
	private final GameUserRepository gameUserRepository;
	private final GameRepository gameRepository;
	private final GameParticipantRepository gameParticipantRepository;

	public List<UserGameInfoDTO> userGameHistory(String email) {
		GameUser gameUser = gameUserRepository.findByEmail(email).orElseThrow();

		Pageable pageable = PageRequest.of(0, 10);
		Page<GameParticipant> gameParticipants =
			gameParticipantRepository.findByGameUserOrderByGameStartAtDesc(gameUser, pageable);

		log.info(gameParticipants.toString());

		return gameParticipantToUserGameInfoDTO(gameParticipants.getContent());
	}

	public List<GameParticipantDTO> gameDetail(String gameCode) {
		List<GameParticipant> gameParticipants = gameParticipantRepository.findByGameGameCode(gameCode);
		return gameParticipantToGameParticipantDTO(gameParticipants);
	}

	public List<UserGameInfoDTO> gameParticipantToUserGameInfoDTO(List<GameParticipant> gameParticipants) {
		return gameParticipants.stream()
			.map(gameParticipant -> UserGameInfoDTO.builder()
				.gameCode(gameParticipant.getGame().getGameCode())
				.gameCategory(gameParticipant.getGame().getGameCategory())
				.rank(gameParticipant.getUserRank())
				.build())
			.collect(Collectors.toList());
	}

	public List<GameParticipantDTO> gameParticipantToGameParticipantDTO(List<GameParticipant> gameParticipants) {
		return gameParticipants.stream()
			.map(gameParticipant -> GameParticipantDTO.builder()
				.nickname(gameParticipant.getGameUser().getNickname())
				.score(gameParticipant.getScore())
				.userRank(gameParticipant.getUserRank())
				.build())
			.collect(Collectors.toList());
	}
}
