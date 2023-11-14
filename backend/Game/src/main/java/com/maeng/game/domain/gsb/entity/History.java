package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class History {

    private int gold;
    private int silver;
    private int bronze;
    private int weight;
    private int bettingChips;
    private int chipsChange;
    private WinDrawLose winDrawLose;

}
