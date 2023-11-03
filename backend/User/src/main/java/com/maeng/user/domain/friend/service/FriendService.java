package com.maeng.user.domain.friend.service;

import org.springframework.stereotype.Service;

import com.maeng.user.domain.friend.entity.Friend;
import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.repository.FriendRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final FriendRepository friendRepository;

	public void addFriend(FriendRequest friendRequest) {
		friendRepository.save(Friend.builder()
			.requester(friendRequest.getRequester())
			.recipient(friendRequest.getRecipient())
			.build());
	}

	public void deleteFriend(String friendId) {
		friendRepository.deleteById(friendId);
	}
}
