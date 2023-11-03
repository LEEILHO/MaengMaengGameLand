package com.maeng.game.domain.awrsp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class History {
    private LocalDateTime submitAt; // 제출 시간
    private Card[] card; // 제출 카드
    private int win; // 승 수
    private int draw; // 비김 수
}
