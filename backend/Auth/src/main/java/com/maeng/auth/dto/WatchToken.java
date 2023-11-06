package com.maeng.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchToken {
    private String watchAccessToken;
    private String watchRefreshToken;

}
