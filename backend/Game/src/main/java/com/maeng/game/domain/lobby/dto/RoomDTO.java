package com.maeng.game.domain.lobby.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoomDTO {
    private String roomCode;
    private String title;
    private int headCount;
    private int maxHeadCount;
}
