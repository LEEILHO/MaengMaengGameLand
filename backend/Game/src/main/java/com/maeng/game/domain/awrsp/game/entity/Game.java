package com.maeng.game.domain.awrsp.game.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RedisHash("awrsp")
public class Game {

    @Id
    private String id; // gameCode
    private String roomCode;
    private LocalDateTime startedAt;
    private LocalDateTime roundStartedAt;
    private int headCount;
    private int currentRound;
    private List<Player> players;
    private Card[] problem;
}
