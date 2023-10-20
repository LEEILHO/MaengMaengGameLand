package com.maeng.jwac.domain.game.entity;

import java.util.Map;

import com.maeng.jwac.domain.game.emums.Tier;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Player {
	private String nickname;
	private String profileUrl;
	private Tier tier;
	private int score;
	private long totalBidAmount;
	private boolean specialItem;
	private Map<Integer, History> history;
}
