package com.maeng.user.domain.friend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public void requestFriend(@RequestHeader("userEmail") String email, @RequestBody FriendRequestDTO friendRequestDTO) {
		User requestUser = userService.getUserByEmail(email);
		User recipUser = userService.getUserByEmail(friendRequestDTO.getRecipientEmail());
		friendService.checkFriend(requestUser, recipUser);
		friendRequestService.requestFriend(requestUser, recipUser);
	}

	@PostMapping("/accept")
	public ResponseEntity<?> acceptFriend(@RequestHeader("userEmail") String email, @RequestBody FriendIdDTO friendIdDTO) {
		FriendRequest friendRequest = friendRequestService.acceptFriend(email, friendIdDTO);

		friendService.addFriend(friendRequest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/reject")
	public ResponseEntity<Void> rejectFriend(@RequestHeader("userEmail") String email, @RequestBody FriendIdDTO friendIdDTO) {
		friendRequestService.deleteFriendRequest(email, friendIdDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/cancel")
	public ResponseEntity<Void> cancelRequest(@RequestHeader("userEmail") String email, @RequestBody FriendIdDTO friendIdDTO) {
		friendRequestService.deleteFriendRequest(email, friendIdDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/delete")
	public ResponseEntity<Void> deleteFriend(@RequestHeader("userEmail") String email, @RequestBody FriendIdDTO friendIdDTO) {
		friendService.deleteFriend(email, friendIdDTO);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/request/list")
	public ResponseEntity<List<FriendDTO>> getFriendRequestList(@RequestHeader("userEmail") String email) {
		List<FriendDTO> friendRequestList = friendRequestService.getFriendRequestList(email);
		return ResponseEntity.ok(friendRequestList);
	}

	@GetMapping("/receive/list")
	public ResponseEntity<List<FriendDTO>> getFriendReceiveList(@RequestHeader("userEmail") String email) {
		List<FriendDTO> friendReceiveList = friendRequestService.getFriendReceiveList(email);
		return ResponseEntity.ok(friendReceiveList);
	}

	@GetMapping("/list")
	public ResponseEntity<List<FriendDTO>> getFriendList(@RequestHeader("userEmail") String email) {
		List<FriendDTO> friendList = friendService.getFriendList(email);
		return ResponseEntity.ok(friendList);
	}

}
