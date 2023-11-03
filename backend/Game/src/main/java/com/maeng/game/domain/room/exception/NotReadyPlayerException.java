package com.maeng.game.domain.room.exception;

public class NotReadyPlayerException extends RuntimeException{
    public NotReadyPlayerException(String message){
        super(message);
    }
}
