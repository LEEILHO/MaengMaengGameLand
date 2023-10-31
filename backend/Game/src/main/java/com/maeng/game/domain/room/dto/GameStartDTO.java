package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.room.entity.Player;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameStartDTO {
    private String roomCode;
    private int headCount;
    private List<Player> participant;
}
