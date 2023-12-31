package com.maeng.user.domain.score.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.score.dto.GiveScoreDTO;
import com.maeng.user.domain.score.dto.RankDTO;
import com.maeng.user.domain.score.dto.RankScoreDTO;
import com.maeng.user.domain.score.entity.Score;
import com.maeng.user.domain.score.entity.ScoreRecord;
import com.maeng.user.domain.score.enums.GameCategory;
import com.maeng.user.domain.score.enums.Tier;
import com.maeng.user.domain.score.repository.ScoreRecordRepository;
import com.maeng.user.domain.score.repository.ScoreRepository;
import com.maeng.user.domain.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreService {

	@Value("${score.tier.silver}")
	private int SILVER_SCORE;

	@Value("${score.tier.gold}")
	private int GOLD_SCORE;

	@Value("${score.tier.challenger}")
	private int CHALLENGER_SCORE;


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

		updateTier(scoreMap);

		scoreRepository.saveAll(scoreMap.values());
		scoreRecordRepository.saveAll(scoreRecords);
	}

	@Transactional
	public Map<String, Score> getOrCreateScore(RankScoreDTO rankScoreDTO) {
		Map<String, Score> scores = new HashMap<>();
		for(String nickname : rankScoreDTO.getRankScoreMap().keySet()) {
			try {
				scores.put(nickname, scoreRepository.findByUser_Nickname(nickname).orElse(Score.builder()
					.user(userRepository.findByNickname(nickname).orElseThrow())
					.build()).addScore(rankScoreDTO.getRankScoreMap().get(nickname)));
			} catch (Exception e) {
				log.info("nickname 정보 없음 {}", nickname);
			}
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

	private void updateTier(Map<String, Score> scores) {
		for(Score score : scores.values()) {
			if(score.getScore() >= CHALLENGER_SCORE) {
				score.updateTier(Tier.CHALLENGER);
			} else if(score.getScore() >= GOLD_SCORE) {
				score.updateTier(Tier.GOLD);
			} else if(score.getScore() >= SILVER_SCORE) {
				score.updateTier(Tier.SILVER);
			} else {
				score.updateTier(Tier.BRONZE);
			}
		}
	}

	public void giveUserScoreAdmin(GiveScoreDTO giveScoreDTO) {
		String nickname = giveScoreDTO.getNickname();
		Score score = scoreRepository.findByUser_Nickname(nickname).orElse(Score.builder()
			.user(userRepository.findByNickname(nickname).orElseThrow())
			.build()).addScore(giveScoreDTO.getScore());

		Map<String, Score> scoreMap = new HashMap<>();
		scoreMap.put(giveScoreDTO.getNickname(), score);

		updateTier(scoreMap);

		ScoreRecord scoreRecord = ScoreRecord.builder()
			.score(score)
			.earnedScore(giveScoreDTO.getScore())
			.gameCategory(null)
			.build();

		scoreRepository.saveAll(scoreMap.values());
		scoreRecordRepository.save(scoreRecord);
	}
}
