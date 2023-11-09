package com.maeng.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CodeDto {

    @NotBlank
    private String code;

    private String fcmToken;

//    private String state;


}
