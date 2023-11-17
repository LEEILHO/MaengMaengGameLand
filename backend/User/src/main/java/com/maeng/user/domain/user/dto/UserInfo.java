package com.maeng.user.domain.user.dto;

import com.maeng.user.domain.score.enums.Tier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
	private String profile;
	private Tier tier;
}
