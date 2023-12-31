package com.maeng.game.domain.gsb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BettingResponseDto {
    private String nextPlayer;
    private String currentPlayer;
    private int currentChips;
    private int totalChips;
    private int timer;
    private int currentPlayerChips;
    private int carryOverChips;
}
