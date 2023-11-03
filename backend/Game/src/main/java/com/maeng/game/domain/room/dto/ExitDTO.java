package com.maeng.game.domain.room.dto;

import lombok.Data;

@Data
public class ExitDTO {
    private String roomCode;
    private String nickname;
    private int seatNumber;
}
