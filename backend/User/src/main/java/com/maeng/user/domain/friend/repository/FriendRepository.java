package com.maeng.user.domain.friend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maeng.user.domain.friend.entity.Friend;
import com.maeng.user.domain.user.entity.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
	@Modifying
	@Query("DELETE FROM friend f "
		+ "WHERE (f.friendId = :friendId AND f.recipient.email = :email) "
		+ "OR (f.friendId = :friendId AND f.requester.email = :email)")
	void deleteByFriendIdAndEmail(UUID friendId, String email);


	Optional<Object> findByRequesterAndRecipient(User requestUser, User recipUser);

	@Query("SELECT f FROM friend f "
		+ "WHERE f.requester.email = :email OR f.recipient.email = :email")
	List<Friend> findAllFriendsByUser(String email);
}
