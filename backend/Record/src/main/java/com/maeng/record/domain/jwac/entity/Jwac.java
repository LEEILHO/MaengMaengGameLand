package com.maeng.record.domain.jwac.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Jwac {
	@Id
	private String gameCode;
	private String roomCode;
	private LocalDateTime createAt;
	private LocalDateTime roundStartAt;
	private int headCount;
	private int currentRound;
	private int maxRound;
	private Map<Integer, Long> bidAmounts;
	private Map<Integer, String> jwerly;
	private Map<String, Player> players;
}
