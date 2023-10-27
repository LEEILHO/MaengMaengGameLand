package com.maeng.user.entity;

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
    private String profileImage;

    @Builder
    public User( String email, String nickname, String profileImage){
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
