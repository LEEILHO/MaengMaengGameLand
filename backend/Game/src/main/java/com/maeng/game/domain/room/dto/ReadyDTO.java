package com.maeng.game.domain.room.dto;

import lombok.Data;

@Data
public class ReadyDTO {
    private String roomCode;
    private String nickname;
    private boolean ready;
}
