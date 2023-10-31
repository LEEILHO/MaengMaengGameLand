package com.maeng.game.domain.jwac.dto;

import java.util.Map;

import com.maeng.game.domain.jwac.entity.Player;

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
	private Map<String, Player> players;
}
