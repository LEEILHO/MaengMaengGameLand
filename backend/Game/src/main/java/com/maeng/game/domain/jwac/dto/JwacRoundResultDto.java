package com.maeng.game.domain.jwac.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwacRoundResultDto {
	private String gameCode;
	private String mostBidder;
	private String leastBidder;
	private Long roundBidSum;
	private Map<String, Player> players;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class Player {
		private int score;
		private boolean item;
	}
}
