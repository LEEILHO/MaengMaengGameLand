package com.maeng.game.domain.jwac.dto;

import com.maeng.game.domain.jwac.emums.Jwerly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacRoundInfoDTO {
	private String gameCode;
	private int currentRound;
	private Jwerly currentJwerly;
	private int currentJwerlyScore;
	private String currentJwerlyImage;
}
