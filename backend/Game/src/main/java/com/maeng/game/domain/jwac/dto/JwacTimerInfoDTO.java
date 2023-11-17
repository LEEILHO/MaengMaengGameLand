package com.maeng.game.domain.jwac.dto;

import com.maeng.game.domain.jwac.emums.Jewelry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacTimerInfoDTO {
	private String gameCode;
	private int round;
	private int time;
	private Jewelry jewelry;
	private int jewelryScore;
}
