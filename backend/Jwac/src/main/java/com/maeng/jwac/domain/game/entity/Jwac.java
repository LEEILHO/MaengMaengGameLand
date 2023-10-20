package com.maeng.jwac.domain.game.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.maeng.jwac.domain.game.emums.Jwerly;

import lombok.Builder;
import lombok.Getter;

@Getter
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
	private List<Long> bidAmounts;
	private List<Jwerly> jwerly;
	private List<Player> players;
}
