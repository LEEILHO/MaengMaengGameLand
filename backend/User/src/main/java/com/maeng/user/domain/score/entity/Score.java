package com.maeng.user.domain.score.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;

import com.maeng.user.domain.score.enums.Tier;
import com.maeng.user.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scoreSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(name = "score")
    @ColumnDefault("0")
    private int score;

    @ColumnDefault("'BRONZE'")
    @Enumerated(EnumType.STRING)
    private Tier tier;

    public Score addScore(int score) {
        this.score += score;
        return this;
    }
}
