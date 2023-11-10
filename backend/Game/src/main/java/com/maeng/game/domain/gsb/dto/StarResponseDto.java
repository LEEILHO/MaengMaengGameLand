package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarResponseDto {

    private String currentPlayer;
    private int weight;
    private String nextPlayer;
    private int timer;

}
