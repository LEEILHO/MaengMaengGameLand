package com.maeng.game.domain.jwac.dto;

import java.util.Map;

import com.maeng.game.domain.jwac.emums.Jewelry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwacItemResultDTO {
	private Map<Jewelry, Integer> itemResult;
	private String nickname;
}
