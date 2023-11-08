package com.maeng.game.domain.jwac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwacRank implements Comparable<JwacRank> {
	private String nickname;
	private Integer score;
	private Long totalBidAmount;

	@Override
	public int compareTo(JwacRank o) {
		if(this.score == o.score) {
			return Long.compare(this.totalBidAmount, o.totalBidAmount);
		} else {
			return Integer.compare(o.score, this.score);
		}
	}
}
