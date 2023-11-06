package com.maeng.user.domain.friend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maeng.user.domain.friend.entity.Friend;
import com.maeng.user.domain.user.entity.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
	void deleteByFriendId(UUID friendId);

	Optional<Object> findByRequesterAndRecipient(User requestUser, User recipUser);

	@Query("SELECT f FROM friend f "
		+ "WHERE f.requester.userSeq = :userSeq OR f.recipient.userSeq = :userSeq")
	List<Friend> findAllFriendsByUser(Long userSeq);
}
