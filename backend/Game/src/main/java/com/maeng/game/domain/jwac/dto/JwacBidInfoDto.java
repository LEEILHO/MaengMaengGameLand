package com.maeng.game.domain.jwac.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwacBidInfoDto {
	private String gameCode;
	private String uerEmail;
	private int round;
	private int bidAmount;
}
