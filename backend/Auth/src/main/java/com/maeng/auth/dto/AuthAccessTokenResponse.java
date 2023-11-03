package com.maeng.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class AuthAccessTokenResponse {
    @NotBlank
    private String accessToken;
}
