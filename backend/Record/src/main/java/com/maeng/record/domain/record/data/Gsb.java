package com.maeng.record.domain.record.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maeng.record.domain.record.enums.Tier;
import com.maeng.record.domain.record.enums.WinDrawLose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gsb {
	@JsonProperty("gameCode")
	private String gameCode;

	@JsonProperty("roomCode")
	private String roomCode;

	@JsonProperty("createAt")
	private LocalDateTime createAt;

	@JsonProperty("currentRound")
	private int currentRound;

	@JsonProperty("carryOverChips")
	private int carryOverChips;

	@JsonProperty("currentPlayer")
	private String currentPlayer;

	@JsonProperty("startCards")
	private StartCard[] startCards;

	@JsonProperty("players")
	private Map<Integer, Player> players;

	@JsonProperty("participants")
	private List<User> participants;

	@JsonProperty("results")
	private List<Result> results;


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StartCard {
		@JsonProperty("seq")
		private int seq;

		@JsonProperty("nickname")
		private String nickname;

		@JsonProperty("selected")
		private boolean selected;
	}



	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Player {
		@JsonProperty("nickname")
		private String nickname;

		@JsonProperty("profileUrl")
		private String profileUrl;

		@JsonProperty("tier")
		private Tier tier;

		@JsonProperty("currentChips")
		private int currentChips;

		@JsonProperty("currentGold")
		private int currentGold;

		@JsonProperty("currentSilver")
		private int currentSilver;

		@JsonProperty("currentBronze")
		private int currentBronze;

		@JsonProperty("currentWeight")
		private int currentWeight;

		@JsonProperty("histories")
		private Map<Integer, History> histories;

	}


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class History {
		@JsonProperty("gold")
		private int gold;

		@JsonProperty("silver")
		private int silver;

		@JsonProperty("bronze")
		private int bronze;

		@JsonProperty("weight")
		private int weight;

		@JsonProperty("bettingChips")
		private int bettingChips;

		@JsonProperty("chipsChange")
		private int chipsChange;

		@JsonProperty("winDrawLose")
		private WinDrawLose winDrawLose;

	}


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		@JsonProperty("nickname")
		private String nickname;

		@JsonProperty("winDrawLose")
		private WinDrawLose winDrawLose;

		@JsonProperty("resultChips")
		private int resultChips;
	}


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class User {
		@JsonProperty("nickname")
		private String nickname;

		@JsonProperty("ready")
		private boolean ready;

		@JsonProperty("host")
		private boolean host;

		@JsonProperty("profileUrl")
		private String profileUrl;

		@JsonProperty("tier")
		private Tier tier;
	}
}
