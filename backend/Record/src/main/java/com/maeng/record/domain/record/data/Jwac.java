package com.maeng.record.domain.record.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private String createAt;

	@JsonProperty("roundStartAt")
	private String roundStartAt;

	@JsonProperty("headCount")
	private int headCount;

	@JsonProperty("currentRound")
	private int currentRound;

	@JsonProperty("maxRound")
	private int maxRound;

	@JsonProperty("bidAmounts")
	private Map<String, Integer> bidAmounts;

	@JsonProperty("jwerly")
	private Map<String, String> jwerly;

	@JsonProperty("players")
	private Map<String, Player> players;


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
		private int totalBidAmount;

		@JsonProperty("specialItem")
		private boolean specialItem;

		@JsonProperty("history")
		private Map<String, History> history;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class History {
		@JsonProperty("lose")
		private boolean lose;

		@JsonProperty("bidAmount")
		private int bidAmount;

		@JsonProperty("bidAt")
		private String bidAt;

		@JsonProperty("win")
		private boolean win;
	}
}
