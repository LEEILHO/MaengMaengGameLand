package com.maeng.user.domain.friend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FriendExceptionAdvice extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<FriendExceptionResponse> handleUserException(FriendRequestException friendRequestException){
        logger.debug("handleUserException(), exception status : {}, exception message: {}",
                friendRequestException.getHttpStatus(),
                friendRequestException.getMessage());
        return ResponseEntity.status(friendRequestException.getHttpStatus()).body(new FriendExceptionResponse(
            friendRequestException.getMessage()));
    }


}