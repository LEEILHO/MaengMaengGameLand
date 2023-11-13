package com.maeng.record.domain.record.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchGameScoreDTO {
	private int score;
	private LocalDateTime updatedAt;
}
