package com.maeng.user.domain.score.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.score.dto.RankScoreDTO;
import com.maeng.user.domain.score.entity.Score;
import com.maeng.user.domain.score.entity.ScoreRecord;
import com.maeng.user.domain.score.enums.GameCategory;
import com.maeng.user.domain.score.repository.ScoreRecordRepository;
import com.maeng.user.domain.score.repository.ScoreRepository;
import com.maeng.user.domain.user.exception.UserException;
import com.maeng.user.domain.user.exception.UserExceptionCode;
import com.maeng.user.domain.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
	private final UserRepository userRepository;
	private final ScoreRepository scoreRepository;
	private final ScoreRecordRepository scoreRecordRepository;

	public void giveScore(RankScoreDTO rankScoreDTO) {
		List<Score> scores = scoreRepository.findByUser_NicknameIn(new ArrayList<>(rankScoreDTO.getRankScoreMap().keySet()));

		generateScoreRecordAndSave(generateScoreMap(scores),
			rankScoreDTO.getRankScoreMap(), rankScoreDTO.getGameCategory());

	}

	private Map<String, Score> generateScoreMap(List<Score> scores) {
		return scores.stream()
				.collect(Collectors.toMap(score -> score.getUser().getNickname(), score -> score));
	}

	@Transactional
	public void generateScoreRecordAndSave(Map<String, Score> scores, Map<String, Integer> rankScoreMap,
		GameCategory gameCategory) {
		List<ScoreRecord> scoreRecords = new ArrayList<>();
		for(String nickname : rankScoreMap.keySet()) {
			Score score = scores.getOrDefault(nickname, Score.builder()
					.user(userRepository.findUserByNickname(nickname).orElseThrow(() -> new UserException(
						UserExceptionCode.USER_NOT_FOUND)))
					.score(0)
					.gameCategory(gameCategory)
				.build());

			ScoreRecord scoreRecord = ScoreRecord.builder()
				.score(score)
				.earnedScore(rankScoreMap.get(nickname))
				.build();
		}
	}

}
