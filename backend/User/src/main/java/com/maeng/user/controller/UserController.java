package com.maeng.user.controller;

import com.maeng.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
