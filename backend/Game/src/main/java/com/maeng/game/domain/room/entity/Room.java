package com.maeng.game.domain.room.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RoomInfo")
public class Room implements Serializable {

    @Id
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private int maxHeadCount;
    private int headCount;
    private boolean publicRoom;
    private List<Player> participant;
    private Game gameCategory;

}
