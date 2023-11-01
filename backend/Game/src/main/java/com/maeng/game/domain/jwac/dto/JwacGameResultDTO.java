package com.maeng.game.domain.jwac.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacGameResultDTO {
	private String roomCode;
	private String gameCode;
	private String winner;
	private Map<String, JwacRoundPlayerInfoDTO> players;
}
