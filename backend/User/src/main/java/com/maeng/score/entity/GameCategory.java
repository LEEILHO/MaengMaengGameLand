package com.maeng.score.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "game_categories")
public class GameCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_category_seq")
    private long gameCategorySeq;

    @Column(name = "name")
    private String name;

}
