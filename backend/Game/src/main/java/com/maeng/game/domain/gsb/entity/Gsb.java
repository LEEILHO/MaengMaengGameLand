package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@RedisHash(value = "Gsb")
public class Gsb {
    @Id
    private String gameCode;
    private String roomCode;
    private LocalDateTime createAt;
    private LocalDateTime roundStartAt;
    private int currentRound;
    private int carryOverChips;
    private StartCard[] startCards;
    private Map<Integer, Player> players;

}
