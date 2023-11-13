package com.maeng.user.domain.score.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.score.dto.RankDTO;
import com.maeng.user.domain.score.dto.RankScoreDTO;
import com.maeng.user.domain.score.entity.Score;
import com.maeng.user.domain.score.entity.ScoreRecord;
import com.maeng.user.domain.score.enums.GameCategory;
import com.maeng.user.domain.score.repository.ScoreRecordRepository;
import com.maeng.user.domain.score.repository.ScoreRepository;
import com.maeng.user.domain.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
	private final UserRepository userRepository;
	private final ScoreRepository scoreRepository;
	private final ScoreRecordRepository scoreRecordRepository;

	public RankScoreDTO generateRankScoreDTO(int weight, RankDTO rankDTO, GameCategory gameCategory) {
		Map<String, Integer> rankScoreMap = generateRankScoreMap(weight, rankDTO);

		return RankScoreDTO.builder()
			.gameCode(rankDTO.getGameCode())
			.rankScoreMap(rankScoreMap)
			.gameCategory(gameCategory)
			.build();
	}

	public Map<String, Integer> generateRankScoreMap(int weight, RankDTO rankDTO) {
		List<String> rankList = rankDTO.getRankList();
		Map<String, Integer> rankScoreMap = new HashMap<>();
		int headCount = rankDTO.getRankList().size();
		int rankScore = 10 * headCount * weight;

		for(int i = 0; i < headCount; i++) {
			rankScore /= 2;

			rankScoreMap.put(rankList.get(i), rankScore);
		}

		return rankScoreMap;
	}

	@Transactional
	public void giveScore(RankScoreDTO rankScoreDTO) {
		Map<String, Score> scoreMap = getOrCreateScore(rankScoreDTO);

		List<ScoreRecord> scoreRecords = generateScoreRecord(rankScoreDTO, scoreMap);

		editWinLose(rankScoreDTO, scoreMap);

		scoreRepository.saveAll(scoreMap.values());
		scoreRecordRepository.saveAll(scoreRecords);
	}

	@Transactional
	public Map<String, Score> getOrCreateScore(RankScoreDTO rankScoreDTO) {
		Map<String, Score> scores = new HashMap<>();
		for(String nickname : rankScoreDTO.getRankScoreMap().keySet()) {
			scores.put(nickname, scoreRepository.findByUser_Nickname(nickname).orElse(Score.builder()
					.user(userRepository.findByNickname(nickname).orElseThrow())
					.build()).addScore(rankScoreDTO.getRankScoreMap().get(nickname)));
		}
		return scores;
	}

	private List<ScoreRecord> generateScoreRecord(RankScoreDTO rankScoreDTO, Map<String, Score> scoreMap) {
		List<ScoreRecord> scoreRecords = new ArrayList<>();
		for(String nickname : rankScoreDTO.getRankScoreMap().keySet()) {
			scoreRecords.add(ScoreRecord.builder()
					.score(scoreMap.get(nickname))
					.earnedScore(rankScoreDTO.getRankScoreMap().get(nickname))
					.gameCategory(rankScoreDTO.getGameCategory())
					.build());
		}

		return scoreRecords;
	}

	private void editWinLose(RankScoreDTO rankScoreDTO, Map<String, Score> scoreMap) {
		List<String> rankList = new ArrayList<>(rankScoreDTO.getRankScoreMap().keySet());
		int headCount = rankList.size();

		if(headCount > 0) {
			scoreMap.get(rankList.get(0)).addWin();
		}

		for(int i = 1; i < headCount; i++) {
			scoreMap.get(rankList.get(i)).addLose();
		}
	}
}
