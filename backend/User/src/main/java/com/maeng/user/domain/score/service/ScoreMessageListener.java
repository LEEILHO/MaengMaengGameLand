package com.maeng.user.domain.score.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maeng.user.domain.score.dto.RankDTO;
import com.maeng.user.domain.score.dto.RankScoreDTO;
import com.maeng.user.domain.score.enums.GameCategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreMessageListener {

	@Value("${score.weight.jwac}")
	private int JWAC_WEIGHT;

	@Value("${score.weight.gsb}")
	private int GSB_WEIGHT;

	@Value("${score.weight.awrsp}")
	private int AWRSP_WEIGHT;

	private final ScoreService scoreService;

	@RabbitListener(queues = "score.jwac")
	public void receiveJwacMessage(RankDTO rankDTO) {
		log.info("jwac message: {}", rankDTO);

		RankScoreDTO rankScoreDTO = scoreService.generateRankScoreDTO(JWAC_WEIGHT, rankDTO, GameCategory.JEWELRY_AUCTION);

		scoreService.giveScore(rankScoreDTO);
	}

	@RabbitListener(queues = "score.gsb")
	public void receiveGsbMessage(RankDTO rankDTO) {
		log.info("gsb message: {}", rankDTO);

		RankScoreDTO rankScoreDTO = scoreService.generateRankScoreDTO(GSB_WEIGHT, rankDTO, GameCategory.GOLD_SILVER_BRONZE);

		scoreService.giveScore(rankScoreDTO);
	}

	@RabbitListener(queues = "score.awrsp")
	public void receiveAwrspMessage(RankDTO rankDTO) {
		log.info("awrsp message: {}", rankDTO);

		RankScoreDTO rankScoreDTO = scoreService.generateRankScoreDTO(AWRSP_WEIGHT, rankDTO, GameCategory.ALL_WIN_ROCK_SCISSOR_PAPER);

		scoreService.giveScore(rankScoreDTO);
	}

}
