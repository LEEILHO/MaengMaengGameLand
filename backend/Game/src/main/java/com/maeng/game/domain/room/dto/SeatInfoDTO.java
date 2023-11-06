package com.maeng.game.domain.room.dto;

import com.maeng.game.domain.room.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatInfoDTO {
    private boolean open;
    private User user;
}
