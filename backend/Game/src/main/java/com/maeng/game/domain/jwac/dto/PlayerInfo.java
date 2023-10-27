package com.maeng.game.domain.jwac.dto;

import com.maeng.game.domain.jwac.emums.Tier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerInfo {
	private String userEmail;
	private String profileUrl;
	private Tier tier;
}
