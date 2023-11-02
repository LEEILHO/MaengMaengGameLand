package com.maeng.record.domain.jwac.entity;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Player {
	private String profileUrl;
	private String tier;
	private int score;
	private long totalBidAmount;
	private boolean specialItem;
	private Map<Integer, History> history;
}
