package com.maeng.user.domain.friend.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.friend.dto.FriendDTO;
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

	@Transactional
	public void checkFriend(User requestUser, User recipUser) {
		friendRepository.findByRequesterAndRecipient(requestUser, recipUser)
			.ifPresent(friend -> {
				throw new FriendRequestException(FriendExceptionCode.ALREADY_FRIEND);
			});
	}

	@Transactional
	public void addFriend(FriendRequest friendRequest) {
		friendRepository.save(Friend.builder()
			.requester(friendRequest.getRequester())
			.recipient(friendRequest.getRecipient())
			.build());
	}

	@Transactional
	public void deleteFriend(String email, UUID friendId) {
		friendRepository.deleteByFriendIdAndEmail(friendId, email);
	}

	@Transactional(readOnly = true)
	public List<FriendDTO> getFriendList(String email) {
		List<Friend> friends = friendRepository.findAllFriendsByUser(email);
		return friendToFriendDTO(email, friends);
	}

	private List<FriendDTO> friendToFriendDTO(String email, List<Friend> friends) {
		return friends.stream()
			.map(friend -> FriendDTO.builder()
				.id(friend.getFriendId())
				.nickname(friend.getRecipient().getEmail().equals(email) ? friend.getRequester().getNickname() : friend.getRecipient().getNickname())
				.build())
			.collect(Collectors.toList());
	}
}
