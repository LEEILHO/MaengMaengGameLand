package com.maeng.auth.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private long userSeq;

    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profile_image;

    @Builder
    public User( String email, String nickname, String profile_image){
        this.email = email;
        this.nickname = nickname;
        this.profile_image = profile_image;
    }

}
