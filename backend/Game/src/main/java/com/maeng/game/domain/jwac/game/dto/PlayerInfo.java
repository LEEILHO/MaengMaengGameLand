package com.maeng.game.domain.jwac.game.dto;

import com.maeng.game.domain.jwac.game.emums.Tier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerInfo {
	private String nickname;
	private String profileUrl;
	private Tier tier;
}
