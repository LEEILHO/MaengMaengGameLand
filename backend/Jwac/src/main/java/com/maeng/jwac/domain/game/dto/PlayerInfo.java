package com.maeng.jwac.domain.game.dto;

import com.maeng.jwac.domain.game.emums.Tier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerInfo {
	private String nickname;
	private String profileUrl;
	private Tier tier;
}
