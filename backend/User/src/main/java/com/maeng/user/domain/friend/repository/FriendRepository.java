package com.maeng.user.domain.friend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.user.domain.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
	void deleteByFriendId(UUID friendId);
}
