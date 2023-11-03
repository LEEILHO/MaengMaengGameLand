package com.maeng.game.domain.room.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadyDTO {
    private String roomCode;
    private String nickname;
    private boolean ready;
}
