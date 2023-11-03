package com.maeng.game.domain.room.entity;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Room")
public class Room implements Serializable {

    @Id
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private int headCount;
    private int maxHeadCount;
    @Indexed
    private boolean publicRoom;
    private HashMap<String, Player> participant;
    @Indexed
    private Game gameCategory;
    @Indexed
    private ChannelTire channelTire;
    private HashMap<Integer, Seat> seats;

}
