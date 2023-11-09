package com.maeng.game.domain.gsb.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@Data
@Builder
@RedisHash(value = "Timer")
public class Timer {
    @Id
    private String gameCode;
    private Set<String> nicknames;
}
