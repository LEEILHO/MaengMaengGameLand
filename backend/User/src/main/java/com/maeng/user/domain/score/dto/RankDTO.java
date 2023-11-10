package com.maeng.user.domain.score.dto;

import java.util.List;

import lombok.Data;

@Data
public class RankDTO {
	private String gameCode;
	private List<String> rankList;
}
