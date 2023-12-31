package com.maeng.user.domain.friend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.user.entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
	Optional<FriendRequest> findByRequestIdAndRequesterEmail(UUID requestId, String email);

	Optional<FriendRequest> findByRequesterAndRecipient(User requester, User recipient);

	void deleteByRequestIdAndRequesterEmail(UUID requestId, String email);

	List<FriendRequest> findAllByRecipientEmail(String recipientEmail);

	List<FriendRequest> findAllByRequesterEmail(String requesterEmail);
}
