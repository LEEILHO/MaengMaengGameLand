package com.maeng.game.domain.jwac.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwacBidInfoDto {
	private String gameCode;
	private String nickname;
	private int round;
	private long bidAmount;
}
