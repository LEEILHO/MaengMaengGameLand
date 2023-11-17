package com.maeng.game.domain.awrsp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResultDTO {
    private String nickname;
    private int round;
    private int rank;
}
