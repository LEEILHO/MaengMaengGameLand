package com.maeng.score.entity;

import com.maeng.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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
    @Enumerated(EnumType.STRING)
    @ColumnDefault("BRONZE")
    private Tier tier;

    @ManyToOne
    @JoinColumn(name = "game_categories")
    private GameCategory gameCategory;




}
