package com.maeng.game.domain.awrsp.dto;

import com.maeng.game.domain.awrsp.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {
    private String nickname;
    private Card[] card;
    private LocalDateTime submitAt;
}
