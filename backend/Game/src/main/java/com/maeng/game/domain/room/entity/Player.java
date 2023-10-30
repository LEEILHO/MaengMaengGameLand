package com.maeng.game.domain.room.entity;

import com.maeng.game.domain.jwac.game.emums.Tier;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {
    private String nickname;
    private boolean ready;
    private boolean host;
    private String profileUrl;
    private Tier tier;
}
