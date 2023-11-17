package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Result {

    private String nickname;
    private WinDrawLose winDrawLose;
    private int resultChips;
}
