package com.maeng.friend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(FriendRequestException friendRequestException){
        logger.debug("handleUserException(), exception status : {}, exception message: {}",
                friendRequestException.getHttpStatus(),
                friendRequestException.getMessage());
        return ResponseEntity.status(friendRequestException.getHttpStatus()).body(new ExceptionResponse(
            friendRequestException.getMessage()));
    }


}