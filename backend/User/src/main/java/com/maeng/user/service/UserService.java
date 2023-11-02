package com.maeng.user.service;

import com.maeng.score.entity.Tier;
import com.maeng.score.repository.ScoreRepository;
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
//    private final ScoreRepository scoreRepository;


    public UserDetailResponse getUserDetail(String userEmail){
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

        /*더미 데이터로*/
        return UserDetailResponse.builder()
                .userEmail(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfileImage())
                .tier(Tier.BRONZE)
                .score(0)
                .win(0)
                .lose(0)
                .build();


    }



}
