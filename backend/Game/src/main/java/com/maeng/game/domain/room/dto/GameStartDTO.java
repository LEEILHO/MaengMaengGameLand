package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.room.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameStartDTO {
    private String roomCode;
    private int headCount;
    private String gameCode;
    private List<User> participant;
}
