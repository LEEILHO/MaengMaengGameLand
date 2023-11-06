package com.maeng.game.domain.gsb.entity;

import com.maeng.game.domain.jwac.emums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Player {
    private String profileUrl;
    private Tier tier;
    private int currentChips;
    private int currentGold;
    private int currentSilver;
    private int currentBronze;
    

}
