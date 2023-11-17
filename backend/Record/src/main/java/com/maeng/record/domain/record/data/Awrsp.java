package com.maeng.record.domain.record.data;

import java.time.LocalDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maeng.record.domain.record.enums.Card;
import com.maeng.record.domain.record.enums.Tier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Awrsp {
    @JsonProperty("id")
    private String id; // gameCode

    @JsonProperty("roomCode")
    private String roomCode;

    @JsonProperty("startedAt")
    private LocalDateTime startedAt;

    @JsonProperty("roundStartedAt")
    private LocalDateTime roundStartedAt;

    @JsonProperty("headCount")
    private int headCount;

    @JsonProperty("currentRound")
    private int currentRound;

    @JsonProperty("players")
    private HashMap<String, Player> players;

    @JsonProperty("problem")
    private Card[] problem;

    @JsonProperty("finishCount")
    private int finishCount;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Player {
        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("profileUrl")
        private String profileUrl;

        @JsonProperty("tier")
        private Tier tier;

        @JsonProperty("finish")
        private boolean finish;

        @JsonProperty("finishedAt")
        private LocalDateTime finishedAt;

        @JsonProperty("finishRound")
        private int finishRound;

        @JsonProperty("histories")
        private HashMap<Integer, History> histories; // <round, 해당 라운드 정보>

        @JsonProperty("rank")
        private int rank;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class History {
        @JsonProperty("submitAt")
        private LocalDateTime submitAt; // 제출 시간

        @JsonProperty("card")
        private Card[] card; // 제출 카드

        @JsonProperty("win")
        private int win; // 승 수

        @JsonProperty("draw")
        private int draw; // 비김 수
    }
}
