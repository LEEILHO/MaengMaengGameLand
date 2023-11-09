package com.maeng.game.domain.gsb.dto;

import com.maeng.game.domain.gsb.entity.Player;
import com.maeng.game.domain.gsb.entity.WinDrawLose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResultDto {

    private boolean isDraw;

    private String winner;
    private int winnerChips;
    private String loser;
    private int loserChips;

}
