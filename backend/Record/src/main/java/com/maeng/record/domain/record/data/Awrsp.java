package com.maeng.record.domain.record.data;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.data.annotation.Id;

import com.maeng.record.domain.record.enums.Card;
import com.maeng.record.domain.record.enums.Tier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Awrsp {

    @Id
    private String id; // gameCode
    private String roomCode;
    private LocalDateTime startedAt;
    private LocalDateTime roundStartedAt;
    private int headCount;
    private int currentRound;
    private HashMap<String, Player> players;
    private Card[] problem;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Player {
        private String nickname;
        private String profileUrl;
        private Tier tier;
        private boolean finish;
        private LocalDateTime finishedAt;
        private HashMap<Integer, History> histories; // <round, 해당 라운드 정보>
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class History {
        private LocalDateTime submitAt; // 제출 시간
        private Card[] card; // 제출 카드
        private int win; // 승 수
        private int draw; // 비김 수
    }
}
