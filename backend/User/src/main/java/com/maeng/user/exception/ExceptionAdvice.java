package com.maeng.user.exception;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException userException){
        logger.debug("handleUserException(), exception status : {}, exception message: {}",
                userException.getHttpStatus(),
                userException.getMessage());
        return ResponseEntity.status(userException.getHttpStatus()).body(new ExceptionResponse(userException.getMessage()));
    }


}