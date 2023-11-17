package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.jwac.emums.Tier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	private String profile;
	private Tier tier;
}
