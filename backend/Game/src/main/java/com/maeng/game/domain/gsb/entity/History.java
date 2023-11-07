package com.maeng.game.domain.gsb.entity;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class History {

    private int gold;
    private int silver;
    private int bronze;
    private int bettingChips;
    private int chipsChange;
    private WinDrawLose winDrawLose;

}
