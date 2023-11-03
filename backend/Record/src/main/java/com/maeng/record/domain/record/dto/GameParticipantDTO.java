package com.maeng.record.domain.record.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameParticipantDTO {
	private String nickname;
	private Integer score;
	private Integer userRank;
}
