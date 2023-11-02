package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.entity.Game;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CreateRoomDTO {
    private String title;
    private boolean publicRoom;
    private Game gameCategory;
    private ChannelTire channelTire;
}
