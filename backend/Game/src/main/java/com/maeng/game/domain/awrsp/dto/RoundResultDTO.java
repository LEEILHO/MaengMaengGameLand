package com.maeng.game.domain.awrsp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoundResultDTO {
    private String nickname;
    private boolean finish;
    private int rank;
    private RoundDetailDTO detail;
    private String profileUrl;
}
