package com.maeng.game.domain.room.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seat {
    private boolean available;
    private String nickname;
}
