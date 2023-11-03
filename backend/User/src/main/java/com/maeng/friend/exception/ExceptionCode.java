package com.maeng.friend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    /* User */
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구 요청이 존재하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}