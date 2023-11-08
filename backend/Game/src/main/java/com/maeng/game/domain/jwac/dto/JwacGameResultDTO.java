package com.maeng.game.domain.jwac.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwacGameResultDTO {
	private String roomCode;
	private String gameCode;
	private List<String> rank;
	private List<JwacRoundPlayerInfoDTO> players;
}
