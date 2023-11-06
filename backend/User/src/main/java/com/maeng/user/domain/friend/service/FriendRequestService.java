package com.maeng.user.domain.friend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.user.domain.friend.dto.FriendDTO;
import com.maeng.user.domain.friend.dto.FriendIdDTO;
import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.exception.FriendExceptionCode;
import com.maeng.user.domain.friend.exception.FriendRequestException;
import com.maeng.user.domain.friend.repository.FriendRequestRepository;
import com.maeng.user.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendRequestService {
	private final FriendRequestRepository friendRequestRepository;

	@Transactional
	public void requestFriend(User requester, User recipient) {
		friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
			.ifPresent(friendRequest -> {
				throw new FriendRequestException(FriendExceptionCode.FRIEND_REQUEST_ALREADY_EXIST);
			});

		friendRequestRepository.save(FriendRequest.builder()
			.requester(requester)
			.recipient(recipient)
			.build());

		// TODO: 알림 전송
	}

	@Transactional
	public FriendRequest acceptFriend(String email, FriendIdDTO friendIdDTO) {
		FriendRequest friendRequest = friendRequestRepository.findByRequestIdAndRequesterEmail(friendIdDTO.getId(), email)
			.orElseThrow(() -> new FriendRequestException(FriendExceptionCode.FRIEND_REQUEST_NOT_FOUND));

		friendRequestRepository.delete(friendRequest);

		return friendRequest;
	}

	@Transactional
	public void deleteFriendRequest(String email, FriendIdDTO friendIdDTO) {
		friendRequestRepository.deleteByRequestIdAndRequesterEmail(friendIdDTO.getId(), email);
	}

	@Transactional(readOnly = true)
	public List<FriendDTO> getFriendRequestList(String email) {
		List<FriendRequest> friendRequests = friendRequestRepository.findAllByRequesterEmail(email);
		return friendRequestRequesterToFriendDTO(friendRequests);
	}

	@Transactional(readOnly = true)
	public List<FriendDTO> getFriendReceiveList(String email) {
		List<FriendRequest> friendRequests = friendRequestRepository.findAllByRecipientEmail(email);
		return friendRequestRecipientToFriendDTO(friendRequests);
	}

	private List<FriendDTO> friendRequestRequesterToFriendDTO(List<FriendRequest> friendRequests) {
		return friendRequests.stream()
			.map(request -> FriendDTO.builder()
				.id(request.getRequestId())
				.nickname(request.getRequester().getNickname())
				.build())
			.collect(Collectors.toList());
	}

	private List<FriendDTO> friendRequestRecipientToFriendDTO(List<FriendRequest> friendRequests) {
		return friendRequests.stream()
			.map(request -> FriendDTO.builder()
				.id(request.getRequestId())
				.nickname(request.getRecipient().getNickname())
				.build())
			.collect(Collectors.toList());
	}
}
