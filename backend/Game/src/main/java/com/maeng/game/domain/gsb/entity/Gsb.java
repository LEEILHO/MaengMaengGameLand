package com.maeng.game.domain.gsb.entity;

import com.maeng.game.domain.room.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@RedisHash(value = "Gsb")
public class Gsb {
    @Id
    private String gameCode;
    private String roomCode;
    private LocalDateTime createAt;
    private int currentRound;
    private int carryOverChips;
    private String currentPlayer;
    private StartCard[] startCards;
    private Map<Integer, Player> players;
    private List<User> participants;

    private List<Result> results;
    public void nextRound() {
        this.currentRound++;
//        this.roundStartAt = LocalDateTime.now();
    }
}
