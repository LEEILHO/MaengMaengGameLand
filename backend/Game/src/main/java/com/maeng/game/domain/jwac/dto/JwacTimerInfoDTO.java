package com.maeng.game.domain.jwac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacTimerInfoDTO {
	private String gameCode;
	private int time;
}
