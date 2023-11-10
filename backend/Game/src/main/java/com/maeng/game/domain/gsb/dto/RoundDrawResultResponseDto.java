package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundDrawResultResponseDto {

    private int currentRound;
    private int nextRound;
    private String nextPlayer;
    private int timer;
    private boolean draw;
    private String player1;
    private int currentPlayer1Chips;
    private String player2;
    private int currentPlayer2Chips;
}
