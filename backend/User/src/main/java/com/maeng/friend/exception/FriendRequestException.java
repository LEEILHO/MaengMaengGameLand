package com.maeng.friend.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FriendRequestException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[FriendRequest] " + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}