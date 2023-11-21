package com.maeng.record.domain.record.dto;

import java.time.LocalDateTime;

import com.maeng.record.domain.record.enums.GameCategoty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGameInfoDTO {
	private String gameCode;
	private GameCategoty gameCategory;
	private LocalDateTime startAt;
	private int rank;
}
