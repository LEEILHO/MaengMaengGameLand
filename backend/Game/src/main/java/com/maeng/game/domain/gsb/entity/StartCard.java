package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Data
public class StartCard {

    private int seq;
    private String nickname;
    private boolean selected;
}
