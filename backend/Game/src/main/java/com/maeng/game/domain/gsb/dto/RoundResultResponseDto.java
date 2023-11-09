package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundResultResponseDto {
    private int currentRound;
    private int nextRound;
    private String nextPlayer;
    private int timer;


    private boolean draw;

    private String winner;
    private int winnerGold;
    private int winnerSilver;
    private int winnerBronze;
    private int currentWinnerChips;

    private String loser;
    private int loserGold;
    private int loserSilver;
    private int loserBronze;
    private int currentLoserChips;






}
