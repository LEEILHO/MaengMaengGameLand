package com.maeng.record.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NicknameEditDTO {
	private String email;
	private String nickname;
}
