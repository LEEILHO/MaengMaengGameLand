package com.maeng.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class JwtRedisManager {

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> opsHashJwt;

    @PostConstruct
    private void init() {

        opsHashJwt = redisTemplate.opsForHash();
    }

    public void storeJwt(String userEmail, String token) {
        opsHashJwt.put("JWT", userEmail, token);
    }

    public boolean isJwtExists(String userEmail) {
        return opsHashJwt.hasKey("JWT", userEmail);
    }

    public int deleteJwt(String userEmail) {
        return Long.valueOf(opsHashJwt.delete("JWT", userEmail)).intValue();
    }

}
