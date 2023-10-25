package com.maeng.auth.exception;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class AuthException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[Auth]" + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }


}

