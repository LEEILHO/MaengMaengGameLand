package com.maeng.user.domain.score.dto;

import java.util.Map;

import com.maeng.user.domain.score.enums.GameCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankScoreDTO {
	private String gameCode;
	private Map<String, Integer> rankScoreMap;
	private GameCategory gameCategory;
}
