package com.maeng.jwac.domain.timer.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "Timer")
public class Timer {
	@Id
	private String gameCode;
	private Map<Integer, String> nicknames;
}
