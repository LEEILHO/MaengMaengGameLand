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


    private boolean isDraw;

    private String winner;
    private int winnerGold;
    private int winnerSilver;
    private int winnerBronze;

    private String loser;
    private int loserGold;
    private int loserSilver;
    private int loserBronze;






}
