package com.maeng.record.domain.jwac.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class History {
	private boolean Win;
	private boolean lose;
	private long bidAmount;
	private LocalDateTime bidAt;
}
