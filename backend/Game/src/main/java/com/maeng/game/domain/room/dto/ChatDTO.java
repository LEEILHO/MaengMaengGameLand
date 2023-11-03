package com.maeng.game.domain.room.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
public class ChatDTO {
    private String nickname;
    private String message;
}
