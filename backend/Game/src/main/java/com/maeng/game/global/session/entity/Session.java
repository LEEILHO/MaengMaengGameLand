package com.maeng.game.global.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Session")
public class Session {

    @Id
    private String nickname; // 유저 닉네임
    @Indexed
    private String sessionId; // 세션 아이디
    private String roomCode; // 대기방 코드 : 대기방에 참여 중이면 대기방 코드 있음
    private String gameCode; // 게임 코드 : 게임에 참여 중이면 게임 코드 있음
}
