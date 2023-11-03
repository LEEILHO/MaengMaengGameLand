package com.maeng.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
}