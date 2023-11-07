package com.maeng.game.domain.jwac.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@RedisHash(value = "Enter")
public class Enter {
	@Id
	private String gameCode;
	private Set<String> nicknames;
}
