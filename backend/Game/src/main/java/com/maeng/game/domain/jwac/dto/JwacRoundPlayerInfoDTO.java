package com.maeng.game.domain.jwac.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwacRoundPlayerInfoDTO {
	private int score;
	private boolean item;
	private Long bidSum;
}