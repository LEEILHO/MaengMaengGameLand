package com.maeng.auth.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CodeDto {

    @NotBlank
    private String code;

//    private String state;


}
