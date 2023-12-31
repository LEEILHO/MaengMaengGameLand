package com.maeng.user.domain.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maeng.user.domain.user.dto.UserNicknameDTO;
import com.maeng.user.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/info")
    public ResponseEntity<?> myDetail(@RequestHeader("userEmail") String userEmail) {
        logger.info("myDetail(), userEmail = {}", userEmail);

        return ResponseEntity.ok().body(userService.getUserDetail(userEmail));
    }

    @GetMapping("/watch")
    public ResponseEntity<?> watchCode(@RequestHeader("userEmail") String userEmail) {
        logger.info("myDetail(), userEmail = {}", userEmail);

        return ResponseEntity.ok().body(userService.getWatchCode(userEmail));
    }

    @PostMapping("/edit/nickname")
    public ResponseEntity<Void> editNickname(@RequestHeader("userEmail") String userEmail, @RequestBody UserNicknameDTO userNicknameDTO) {
        logger.info("editNickname(), userEmail = {}, nickname = {}", userEmail, userNicknameDTO.getNickname());

        userService.editNickname(userEmail, userNicknameDTO.getNickname());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/profile")
    public ResponseEntity<Void> editProfile(@RequestHeader("userEmail") String userEmail, @RequestPart("profileImage") MultipartFile profileInage) {
        logger.info("editProfile(), userEmail = {}", userEmail);

        userService.editProfile(userEmail, profileInage);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> getUserInfo(@PathVariable("nickname") String nickname) {
        logger.info("getUserInfo(), nickname = {}", nickname);

        return ResponseEntity.ok().body(userService.getUserInfo(nickname));
    }

}
