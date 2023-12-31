package com.maeng.game.domain.gsb.entity;

import java.util.Map;

import com.maeng.game.domain.jwac.emums.Tier;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Player {
    private String nickname;
    private String profileUrl;
    private Tier tier;
    private int currentChips;
    private int currentGold;
    private int currentSilver;
    private int currentBronze;
    private int currentWeight;
    private Map<Integer, History> histories;

}
