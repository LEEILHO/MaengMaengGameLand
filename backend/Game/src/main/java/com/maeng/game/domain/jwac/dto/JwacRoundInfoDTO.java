package com.maeng.game.domain.jwac.dto;

import com.maeng.game.domain.jwac.emums.Jewelry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacRoundInfoDTO {
	private String gameCode;
	private int currentRound;
	private Jewelry currentJewelry;
	private int currentJewelryScore;
	private String currentJewelryImage;
}
