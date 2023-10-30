package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Player;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoDTO {

    private String title;
    private LocalDateTime createdAt;
    private int maxHeadCount;
    private int headCount;
    private boolean publicRoom;
    private List<Player> participant;
    private Game gameCategory;
}
