package com.maeng.user.domain.user.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class UserException extends RuntimeException {
    private final UserExceptionCode userExceptionCode;

    public String getMessage() {
        return "[User] " + userExceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return userExceptionCode.getStatus();
    }
}