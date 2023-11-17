package com.maeng.user.domain.user.dto;

import com.maeng.user.domain.score.enums.Tier;
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
    private Tier tier;
    private int score;
    private int win;
    private int lose;

}
