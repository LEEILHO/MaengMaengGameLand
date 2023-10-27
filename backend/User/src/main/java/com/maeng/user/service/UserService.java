package com.maeng.user.service;

import com.maeng.user.dto.UserDetailResponse;
import com.maeng.user.entity.User;
import com.maeng.user.exception.ExceptionCode;
import com.maeng.user.exception.UserException;
import com.maeng.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserDetailResponse getUserDetail(String userEmail){
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

        return UserDetailResponse.builder()
                .userEmail(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfileImage())
                .build();


    }



}
