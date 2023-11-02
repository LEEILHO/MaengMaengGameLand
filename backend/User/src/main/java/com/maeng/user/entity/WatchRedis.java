package com.maeng.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "CODE", timeToLive = 30)
public class WatchRedis {
    @Id
    private String email;
    @Indexed
    private String code;
}
