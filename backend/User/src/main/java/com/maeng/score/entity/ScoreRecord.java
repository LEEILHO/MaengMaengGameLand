package com.maeng.score.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "score_records")
public class ScoreRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_record_seq")
    private long scoreRecordSeq;

    @ManyToOne
    @JoinColumn(name = "score_seq")
    private Score score;

    @Column(name = "earned_score")
    private int earnedScore;

    @Column(name = "create_at")
    private LocalDateTime createAt;



}
