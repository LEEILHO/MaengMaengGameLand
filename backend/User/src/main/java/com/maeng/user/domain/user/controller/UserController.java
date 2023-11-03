package com.maeng.user.domain.user.controller;

import com.maeng.user.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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




}
