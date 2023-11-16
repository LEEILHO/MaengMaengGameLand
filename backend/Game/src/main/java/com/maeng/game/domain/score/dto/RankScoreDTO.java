package com.maeng.game.domain.score.dto;

import java.util.Map;

import com.maeng.game.domain.room.entity.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankScoreDTO {
	private String gameCode;
	private Game game;
	private Map<String, Integer> rankScoreMap;
}
