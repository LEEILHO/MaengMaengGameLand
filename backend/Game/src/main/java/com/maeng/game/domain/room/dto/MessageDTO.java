package com.maeng.game.domain.room.dto;

import lombok.*;


@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String type;
    private Object data;
}
