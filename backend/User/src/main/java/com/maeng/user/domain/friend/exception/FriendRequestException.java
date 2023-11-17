package com.maeng.user.domain.friend.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FriendRequestException extends RuntimeException {
    private final FriendExceptionCode friendExceptionCode;

    public String getMessage() {
        return "[FriendRequest] " + friendExceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return friendExceptionCode.getStatus();
    }
}