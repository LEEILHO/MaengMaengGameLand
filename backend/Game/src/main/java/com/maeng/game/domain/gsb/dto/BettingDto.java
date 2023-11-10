package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingDto {
    private boolean giveUp;
    private int bettingChips;
}
