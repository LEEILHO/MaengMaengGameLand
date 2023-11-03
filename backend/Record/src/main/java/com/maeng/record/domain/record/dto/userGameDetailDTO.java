package com.maeng.record.domain.record.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class userGameDetailDTO {
	private String gameCode;
	private String gameCategory;
	private String gameStartAt;
	private List<GameParticipantDTO> gameParticipants;
}
