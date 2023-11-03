package com.maeng.auth.dto;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class WatchCodeDto {
    @NotBlank
    private String watchCode;

}
