package com.maeng.game.domain.score.config;

import lombok.Data;

@Data
public class ScoreConfigProperties {
	private int jwacWeight;
	private int gsbWeight;
	private int awrspWeight;

	public ScoreConfigProperties(int jwacWeight, int gsbWeight, int awrspWeight) {
		this.jwacWeight = jwacWeight;
		this.gsbWeight = gsbWeight;
		this.awrspWeight = awrspWeight;
	}
}
