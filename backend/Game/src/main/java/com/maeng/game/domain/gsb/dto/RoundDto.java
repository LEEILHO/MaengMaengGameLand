package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundDto {
    private int round;
    private int firstPlayer;
    private int timer;
}
