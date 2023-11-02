package com.maeng.friend.entity;


import com.maeng.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_seq")
    private long friendSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_seq")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_seq")
    private User recipient;



}
