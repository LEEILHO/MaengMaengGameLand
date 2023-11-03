package com.maeng.game.domain.room.dto;

import lombok.Data;

@Data
public class KickDTO {
    private String nickname;
    private String kickPlayer;
    private int seatNumber;
}
