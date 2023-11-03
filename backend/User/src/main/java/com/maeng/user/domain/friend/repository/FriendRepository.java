package com.maeng.user.domain.friend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.user.domain.friend.entity.Friend;
import com.maeng.user.domain.user.entity.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
	void deleteByFriendId(UUID friendId);

	Optional<Object> findByRequesterAndRecipient(User requestUser, User recipUser);
}
