//package com.maeng.user.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//public class WatchRedisManager {
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private ValueOperations<String, String> valueOperations;
//
//    @PostConstruct
//    private void init() {
//
//        valueOperations = redisTemplate.opsForValue();
//    }
//
//      public void storeCode(String userEmail, String code) {
//        // 만료기한 설정
//
//        opsHashWatchCode.put(code, userEmail);
//    }
//
//    public boolean isCodeExists(String code) {
//        return opsHashWatchCode.hasKey("CODE", code);
//    }
//
//    public int deleteCode(String code) {
//        return Long.valueOf(opsHashWatchCode.delete("CODE", code)).intValue();
//    }
//
//    public Map<String,String> getCode() {
//        return opsHashWatchCode.entries("CODE");
//    }
//
//}
