package com.maeng.game.domain.awrsp.dto;

import com.maeng.game.domain.awrsp.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {
    private String nickname;
    private Card[] card;
}
