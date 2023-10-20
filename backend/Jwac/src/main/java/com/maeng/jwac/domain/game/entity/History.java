package com.maeng.jwac.domain.game.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class History {
	private boolean bid;
	private long bidAmount;
	private boolean lowBidder;
}
