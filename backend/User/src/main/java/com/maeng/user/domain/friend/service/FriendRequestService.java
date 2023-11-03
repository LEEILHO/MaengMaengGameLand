package com.maeng.user.domain.friend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.exception.FriendExceptionCode;
import com.maeng.user.domain.friend.exception.FriendRequestException;
import com.maeng.user.domain.friend.repository.FriendRequestRepository;
import com.maeng.user.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
	private final FriendRequestRepository friendRequestRepository;

	@Transactional
	public void requestFriend(User requester, User recipient) {
		friendRequestRepository.save(FriendRequest.builder()
			.requester(requester)
			.recipient(recipient)
			.build());

		// TODO: 알림 전송
	}

	@Transactional
	public FriendRequest acceptFriend(UUID requestId) {
		FriendRequest friendRequest = friendRequestRepository.findByRequestId(requestId)
			.orElseThrow(() -> new FriendRequestException(FriendExceptionCode.FRIEND_REQUEST_NOT_FOUND));

		friendRequestRepository.delete(friendRequest);

		return friendRequest;
	}

	@Transactional
	public void deleteFriendRequest(UUID requestId) {
		friendRequestRepository.deleteByRequestId(requestId);
	}

}
