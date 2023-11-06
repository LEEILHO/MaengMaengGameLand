package com.maeng.game.domain.gsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSeqDto {
    private String nickName;
    private int seq;
}
