package com.maeng.game.domain.room.controller;


import com.maeng.game.domain.room.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoomControllerAdvice {

    @ExceptionHandler({NotFoundRoomException.class})
    public ResponseEntity<String> handlerRoomNotFoundException(NotFoundRoomException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler({NotHostException.class})
    public ResponseEntity<String> handlerNotHostException(NotHostException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler({MinHeadCountException.class})
    public ResponseEntity<String> handlerMinHeadCountException(MinHeadCountException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler({NotReadyPlayerException.class})
    public ResponseEntity<String> handlerNotReadyPlayerException(NotReadyPlayerException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler({PullRoomException.class})
    public ResponseEntity<String> handlerPullRoomException(PullRoomException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
