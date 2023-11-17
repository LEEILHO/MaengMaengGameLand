package com.maeng.game.domain.awrsp.dto;

import com.maeng.game.domain.awrsp.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDTO {
    private Card[] problem;
}
