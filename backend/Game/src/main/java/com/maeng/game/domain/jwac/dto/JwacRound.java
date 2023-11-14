package com.maeng.game.domain.jwac.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class JwacRound implements Comparable<JwacRound> {
	private String nickname;
	private Long bidAmount;
	private LocalDateTime bidAt;

	@Override
	public int compareTo(JwacRound o) {
		if (Objects.equals(this.bidAmount, o.bidAmount)) {
			return this.bidAt.compareTo(o.bidAt);
		} else {
			return Long.compare(o.bidAmount, this.bidAmount);
		}
	}
}
