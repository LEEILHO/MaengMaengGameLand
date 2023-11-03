package com.maeng.user.domain.friend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.user.domain.friend.entity.FriendRequest;
import com.maeng.user.domain.friend.service.FriendRequestService;
import com.maeng.user.domain.friend.service.FriendService;
import com.maeng.user.domain.user.entity.User;
import com.maeng.user.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

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
	public void rejectFriend(@RequestParam UUID requestId) {
		friendRequestService.deleteFriendRequest(requestId);
	}

	@PostMapping("/cancel")
	public void cancelRequest(@RequestParam UUID requestId) {
		friendRequestService.deleteFriendRequest(requestId);
	}

	@PostMapping("/delete")
	public void deleteFriend(@RequestParam UUID friendId) {
		friendService.deleteFriend(friendId);
	}

	@PostMapping("/request/list")
	public void getFriendRequestList() {
	}

	@PostMapping("/list")
	public void getFriendList() {
	}

}
