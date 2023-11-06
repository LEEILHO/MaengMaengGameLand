package com.maeng.user.domain.friend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.user.domain.friend.dto.FriendDTO;
import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.service.FriendRequestService;
import com.maeng.user.domain.friend.service.FriendService;
import com.maeng.user.domain.user.entity.User;
import com.maeng.user.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {
	private final UserService userService;
	private final FriendService friendService;
	private final FriendRequestService friendRequestService;

	@PostMapping("/request")
	public void requestFriend(String requester, String recipient) {
		User requestUser = userService.getUserByEmail(requester);
		User recipUser = userService.getUserByEmail(recipient);
		friendService.checkFriend(requestUser, recipUser);
		friendRequestService.requestFriend(requestUser, recipUser);
	}

	@PostMapping("/accept")
	public ResponseEntity<?> acceptFriend(@RequestParam UUID requestId) {
		FriendRequest friendRequest = friendRequestService.acceptFriend(requestId);

		friendService.addFriend(friendRequest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/reject")
	public ResponseEntity<Void> rejectFriend(@RequestParam UUID requestId) {
		friendRequestService.deleteFriendRequest(requestId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/cancel")
	public ResponseEntity<Void> cancelRequest(@RequestParam UUID requestId) {
		friendRequestService.deleteFriendRequest(requestId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/delete")
	public ResponseEntity<Void> deleteFriend(@RequestParam UUID friendId) {
		friendService.deleteFriend(friendId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/request/list")
	public ResponseEntity<List<FriendDTO>> getFriendRequestList(@RequestParam String email) {
		List<FriendDTO> friendRequestList = friendRequestService.getFriendRequestList(email);
		return ResponseEntity.ok(friendRequestList);
	}

	@PostMapping("/receive/list")
	public ResponseEntity<List<FriendDTO>> getFriendReceiveList(@RequestParam String email) {
		List<FriendDTO> friendReceiveList = friendRequestService.getFriendReceiveList(email);
		return ResponseEntity.ok(friendReceiveList);
	}

	@PostMapping("/list")
	public ResponseEntity<List<FriendDTO>> getFriendList() {
		List<FriendDTO> friendList = friendService.getFriendList();
		return ResponseEntity.ok(friendList);
	}

}
