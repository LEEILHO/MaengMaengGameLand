package com.maeng.user.domain.friend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.maeng.user.domain.friend.entity.Friend;
import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.exception.FriendExceptionCode;
import com.maeng.user.domain.friend.exception.FriendRequestException;
import com.maeng.user.domain.friend.repository.FriendRepository;
import com.maeng.user.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final FriendRepository friendRepository;


	public void checkFriend(User requestUser, User recipUser) {
		friendRepository.findByRequesterAndRecipient(requestUser, recipUser)
			.ifPresent(friend -> {
				throw new FriendRequestException(FriendExceptionCode.ALREADY_FRIEND);
			});
	}

	public void addFriend(FriendRequest friendRequest) {
		friendRepository.save(Friend.builder()
			.requester(friendRequest.getRequester())
			.recipient(friendRequest.getRecipient())
			.build());
	}

	public void deleteFriend(UUID friendId) {
		friendRepository.deleteByFriendId(friendId);
	}
}
