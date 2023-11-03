package com.maeng.game.domain.room.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PlayerDTO {
    private String roomCode;
    private String nickname;
}
