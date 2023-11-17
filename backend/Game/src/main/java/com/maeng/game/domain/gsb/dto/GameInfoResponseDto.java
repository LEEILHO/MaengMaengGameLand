package com.maeng.game.domain.gsb.dto;

import com.maeng.game.domain.gsb.entity.Gsb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameInfoResponseDto {
    private Gsb gsb;
    private int timer;
}
