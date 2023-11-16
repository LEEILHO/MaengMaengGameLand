package com.maeng.game.domain.score.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.score.config.ScoreConfigProperties;
import com.maeng.game.domain.score.dto.RankDTO;
import com.maeng.game.domain.score.dto.RankScoreDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreService {
	private final RabbitTemplate template;
	private final ScoreConfigProperties scoreConfigProperties;


	public Map<String, Integer> generateJwacScoreMap(RankDTO rankDTO) {
		return generateRankScoreMap(scoreConfigProperties.getJwacWeight(), rankDTO, Game.JEWELRY_AUCTION);
	}

	public Map<String, Integer> generateGsbScoreMap(RankDTO rankDTO) {
		return generateRankScoreMap(scoreConfigProperties.getGsbWeight(), rankDTO, Game.GOLD_SILVER_BRONZE);
	}

	public Map<String, Integer> generateAwrspScoreMap(RankDTO rankDTO) {
		return generateRankScoreMap(scoreConfigProperties.getAwrspWeight(), rankDTO, Game.ALL_WIN_ROCK_SCISSOR_PAPER);
	}

	public Map<String, Integer> generateRankScoreMap(int weight, RankDTO rankDTO, Game game) {
		List<String> rankList = rankDTO.getRankList();
		Map<String, Integer> rankScoreMap = new HashMap<>();
		int headCount = rankDTO.getRankList().size();
		int rankScore = 10 * headCount * weight;

		for(int i = 0; i < headCount; i++) {
			rankScore /= 2;

			rankScoreMap.put(rankList.get(i), rankScore);
		}

		sendScoreToUserService(rankDTO.getGameCode(), rankScoreMap, game);

		return rankScoreMap;
	}

	private void sendScoreToUserService(String gameCode, Map<String, Integer> rankScoreMap, Game game) {
		try {
			RankScoreDTO rankScoreDTO = RankScoreDTO.builder()
				.gameCode(gameCode)
				.rankScoreMap(rankScoreMap)
				.game(game)
				.build();

			template.convertAndSend("user", "score.jwac", rankScoreDTO);
		} catch(Exception e) {
			log.info("sendScoreToUserService error: " + e.getMessage());
		}
	}
}
