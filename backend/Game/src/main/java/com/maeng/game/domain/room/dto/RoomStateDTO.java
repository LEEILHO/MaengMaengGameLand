package com.maeng.game.domain.room.dto;


import lombok.Data;

@Data
public class RoomStateDTO {
    private String nickname;
    private String title;
    private boolean publicRoom;
}
