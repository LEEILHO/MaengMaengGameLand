package com.maeng.user.service;

import com.maeng.user.config.WatchRedisManager;
import com.maeng.score.entity.Tier;
import com.maeng.user.dto.UserDetailResponse;
import com.maeng.user.dto.WatchCode;
import com.maeng.user.entity.User;
import com.maeng.user.exception.ExceptionCode;
import com.maeng.user.exception.UserException;
import com.maeng.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final ScoreRepository scoreRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WatchRedisManager watchRedisManager;

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
    public WatchCode getWatchCode(String userEmail){
        Random random = new Random();
        int letter = 8;
        String code = "";
        for(int i=0; i<letter; i++){
            int num = random.nextInt(9);
            code +=Integer.toString(num);
        }
        logger.info("getUserWatchCode(), userEmail = {}, code = {}",userEmail,code);
        watchRedisManager.storeCode(userEmail,code);
        return WatchCode.builder()
                .watchCode(code).
                build();




    }




}
