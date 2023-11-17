package com.maeng.game.domain.jwac.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwacGameInfoDTO {
	private String gameCode;
	private List<PlayerInfo> playerInfo;
}
