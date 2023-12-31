package com.maeng.game.domain.jwac.entity;

import java.util.Map;

import com.maeng.game.domain.jwac.emums.Tier;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Player {
	private String profileUrl;
	private Tier tier;
	private int score;
	private long totalBidAmount;
	private boolean specialItem;
	private Map<Integer, History> history;

	public void addScore(int score) {
		this.score += score;
	}

	public void addTotalBidAmount(long bidAmount) {
		this.totalBidAmount += bidAmount;
	}
}
