package com.maeng.game.domain.timer.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;


@Data
@Builder
@RedisHash(value = "Timer")
public class Timer {
	@Id
	private String gameCode;
	private Set<String> nicknames;
}
