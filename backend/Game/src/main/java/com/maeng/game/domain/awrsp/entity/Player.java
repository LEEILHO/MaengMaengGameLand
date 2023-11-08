package com.maeng.game.domain.awrsp.entity;

import com.maeng.game.domain.jwac.emums.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;

@Builder
@Data
@ToString
@AllArgsConstructor
public class Player {
    private String nickname;
    private String profileUrl;
    private Tier tier;
    private boolean finish;
    private LocalDateTime finishedAt;
    private HashMap<Integer, History> histories; // <round, 해당 라운드 정보>
    private int rank;
}
