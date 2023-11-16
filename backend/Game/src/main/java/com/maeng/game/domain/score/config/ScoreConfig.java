package com.maeng.game.domain.score.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScoreConfig {
	@Value("${score.weight.jwac}")
	private int JWAC_WEIGHT;

	@Value("${score.weight.gsb}")
	private int GSB_WEIGHT;

	@Value("${score.weight.awrsp}")
	private int AWRSP_WEIGHT;

	@Bean
	public ScoreConfigProperties scoreConfigProperties() {
		return new ScoreConfigProperties(JWAC_WEIGHT, GSB_WEIGHT, AWRSP_WEIGHT);
	}
}