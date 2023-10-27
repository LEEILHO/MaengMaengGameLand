package com.maeng.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDetailResponse {

    private String userEmail;
    private String nickname;
    private String profile;

}
