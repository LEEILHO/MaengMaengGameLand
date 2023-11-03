package com.maeng.user.domain.friend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendExceptionCode {

    /* User */
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구 요청이 존재하지 않습니다."),
    FRIEND_REQUEST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 친구 요청을 보냈습니다."),
    ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "이미 친구입니다.");


    private final HttpStatus status;
    private final String message;
}