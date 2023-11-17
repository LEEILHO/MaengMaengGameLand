package com.maeng.user.domain.user.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode {

    /* User */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    FAIL_TO_EDIT_PROFILE(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 수정에 실패하였습니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NICKNAME_LENGTH_EXCEED(HttpStatus.BAD_REQUEST, "닉네임은 12자 이내로 입력해주세요.");


    private final HttpStatus status;
    private final String message;
}