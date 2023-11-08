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
public class JwacRoundResultDto {
	private String gameCode;
	private String mostBidder;
	private String leastBidder;
	private Long roundBidSum;
	private int round;
	private int jewelryScore;
	private int penaltyScore;
	private List<PlayerInfo> players;
}
