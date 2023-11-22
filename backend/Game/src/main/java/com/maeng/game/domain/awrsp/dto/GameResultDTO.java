package com.maeng.game.domain.awrsp.dto;

import com.maeng.game.domain.awrsp.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResultDTO {
    private List<RankResultDTO> result;
    private Card[] answer;
}
