package com.maeng.user.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class UserException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[User] " + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}