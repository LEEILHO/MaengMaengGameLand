package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiveUpResponseDto {
    private int currentRound;
    private int nextRound;
    private String nextPlayer;
    private int timer;
    private String winner;
    private int currentWinnerChips;
    private String loser;
    private int currentLoserChips;

}
