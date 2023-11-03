package com.maeng.game.domain.room.exception;

public class NotFoundRoomException extends RuntimeException{
    public NotFoundRoomException(String message){
        super(message);
    }
}
