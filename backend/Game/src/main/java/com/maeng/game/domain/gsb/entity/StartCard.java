package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class StartCard {

    private int seq;
    private String nickname;
    private boolean selected;
}
