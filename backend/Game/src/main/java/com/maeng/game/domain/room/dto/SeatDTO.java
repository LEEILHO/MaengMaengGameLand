package com.maeng.game.domain.room.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SeatDTO {
    private String nickname;
    private int seatNumber;
}
