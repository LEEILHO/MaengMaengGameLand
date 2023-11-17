package com.maeng.user.domain.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionAdvice extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserExceptionResponse> handleUserException(UserException userException){
        logger.debug("handleUserException(), exception status : {}, exception message: {}",
                userException.getHttpStatus(),
                userException.getMessage());
        return ResponseEntity.status(userException.getHttpStatus()).body(new UserExceptionResponse(userException.getMessage()));
    }


}