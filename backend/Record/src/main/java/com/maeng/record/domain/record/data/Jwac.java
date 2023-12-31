package com.maeng.record.domain.record.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maeng.record.domain.record.enums.Jewelry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jwac {
	@JsonProperty("gameCode")
	private String gameCode;

	@JsonProperty("roomCode")
	private String roomCode;

	@JsonProperty("createAt")
	private LocalDateTime createAt;

	@JsonProperty("roundStartAt")
	private String roundStartAt;

	@JsonProperty("headCount")
	private int headCount;

	@JsonProperty("currentRound")
	private int currentRound;

	@JsonProperty("maxRound")
	private int maxRound;

	@JsonProperty("bidAmounts")
	private Map<String, Long> bidAmounts;

	@JsonProperty("jewelry")
	private Map<Integer, Jewelry> jewelry;

	@JsonProperty("players")
	private Map<String, Player> players;

	@JsonProperty("rank")
	private List<String> rank;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Player {
		@JsonProperty("profileUrl")
		private String profileUrl;

		@JsonProperty("tier")
		private String tier;

		@JsonProperty("score")
		private int score;

		@JsonProperty("totalBidAmount")
		private long totalBidAmount;

		@JsonProperty("specialItem")
		private boolean specialItem;

		@JsonProperty("history")
		private Map<Integer, History> history;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class History {
		@JsonProperty("lose")
		private boolean lose;

		@JsonProperty("bidAmount")
		private long bidAmount;

		@JsonProperty("bidAt")
		private LocalDateTime bidAt;

		@JsonProperty("win")
		private boolean win;
	}
}
