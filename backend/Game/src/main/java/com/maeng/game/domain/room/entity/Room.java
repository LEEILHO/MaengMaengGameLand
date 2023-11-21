package com.maeng.game.domain.room.entity;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Room")
@Table( // 복합 unique키 설정 (중복 저장 X)
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"gameCode"}
                )
        }
)
public class Room implements Serializable {

    @Id
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private int headCount;
    private int maxHeadCount;
    private int minHeadCount;
    @Indexed
    private boolean publicRoom;
    private HashMap<String, User> participant;
    @Indexed
    private Game gameCategory;
    @Indexed
    private ChannelTire channelTire;
    private HashMap<Integer, Seat> seats;
    @Indexed
    private String gameCode;
    private boolean gameStart;

}
