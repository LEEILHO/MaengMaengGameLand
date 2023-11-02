package com.maeng.score.entity;

import com.maeng.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_seq")
    private long scoreSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;


    @Column(name = "score")
    private int score;

    @Column(name = "tier")
    private Tier tier;

    @ManyToOne
    @JoinColumn(name = "game_categories")
    private GameCategory gameCategory;




}
