package com.maeng.user.domain.friend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.user.domain.friend.dto.FriendDTO;
import com.maeng.user.domain.friend.dto.FriendIdDTO;
import com.maeng.user.domain.friend.dto.FriendRequestDTO;
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
	public void requestFriend(@RequestParam String email, @RequestBody FriendRequestDTO friendRequestDTO) {
		User requestUser = userService.getUserByEmail(email);
		User recipUser = userService.getUserByEmail(friendRequestDTO.getRecipientEmail());
		friendService.checkFriend(requestUser, recipUser);
		friendRequestService.requestFriend(requestUser, recipUser);
	}

	@PostMapping("/accept")
	public ResponseEntity<?> acceptFriend(@RequestParam String email, @RequestBody FriendIdDTO friendIdDTO) {
		FriendRequest friendRequest = friendRequestService.acceptFriend(email, friendIdDTO);

		friendService.addFriend(friendRequest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/reject")
	public ResponseEntity<Void> rejectFriend(@RequestParam String email, @RequestBody FriendIdDTO friendIdDTO) {
		friendRequestService.deleteFriendRequest(email, friendIdDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/cancel")
	public ResponseEntity<Void> cancelRequest(@RequestParam String email, @RequestBody FriendIdDTO friendIdDTO) {
		friendRequestService.deleteFriendRequest(email, friendIdDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/delete")
	public ResponseEntity<Void> deleteFriend(@RequestParam String email, @RequestParam UUID friendId) {
		friendService.deleteFriend(email, friendId);
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
