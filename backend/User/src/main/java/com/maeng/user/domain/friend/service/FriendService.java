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
import com.maeng.user.domain.user.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final UserRepository UserRepository;
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
	public List<FriendDTO> getFriendList() {
		User user  = UserRepository.findUserByEmail("seg412@naver.com").orElseThrow();
		List<Friend> friends = friendRepository.findAllFriendsByUser(user.getUserSeq());
		return friendToFriendDTO(user.getNickname(), friends);
	}

	private List<FriendDTO> friendToFriendDTO(String nickname, List<Friend> friends) {
		return friends.stream()
			.map(friend -> FriendDTO.builder()
				.id(friend.getFriendId())
				.nickname(friend.getRecipient().getNickname().equals(nickname) ? friend.getRequester().getNickname() : friend.getRecipient().getNickname())
				.build())
			.collect(Collectors.toList());
	}
}
