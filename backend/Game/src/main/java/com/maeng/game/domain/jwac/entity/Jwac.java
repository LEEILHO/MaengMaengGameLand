package com.maeng.game.domain.jwac.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.maeng.game.domain.jwac.emums.Jewelry;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash(value = "Jwac")
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
	private Map<Integer, Jewelry> jewelry;
	private Map<String, Player> players;

	public void nextRound() {
		this.currentRound++;
		this.roundStartAt = LocalDateTime.now();
	}
}
