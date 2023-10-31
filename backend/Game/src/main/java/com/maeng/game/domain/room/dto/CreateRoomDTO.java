package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.entity.Game;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
public class CreateRoomDTO {
    private String title;
    private boolean publicRoom;
    private LocalDateTime createdAt;
    private String host;
    private Game gameCategory;
    private ChannelTire channelTire;
}
