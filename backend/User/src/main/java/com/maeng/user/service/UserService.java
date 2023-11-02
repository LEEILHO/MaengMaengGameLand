package com.maeng.user.service;

//import com.maeng.user.config.WatchRedisManager;
import com.maeng.score.entity.Tier;
import com.maeng.user.dto.UserDetailResponse;
import com.maeng.user.dto.WatchCode;
import com.maeng.user.entity.User;
import com.maeng.user.entity.WatchRedis;
import com.maeng.user.exception.ExceptionCode;
import com.maeng.user.exception.UserException;
import com.maeng.user.respository.UserRepository;
import com.maeng.user.respository.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final ScoreRepository scoreRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final WatchRedisManager watchRedisManager;
    private final WatchRepository watchRepository;

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
    @Transactional
    public WatchCode getWatchCode(String userEmail){
//        Map<String, String> Codes =watchRedisManager.getCode();
//
//        List<String> keys = new ArrayList<>();
//        for (Map.Entry<String, String> entry : Codes.entrySet()) {
//            if (userEmail.equals(entry.getValue())) {
//                keys.add(entry.getKey());
//            }
//        }
//        // 삭제
//        for(String key: keys){
//            watchRedisManager.deleteCode(key);
//        }
        Random random = new Random();
        int letter = 8;
        String code = "";
        for(int i=0; i<letter; i++){
            int num = random.nextInt(9);
            code +=Integer.toString(num);
        }
        logger.info("getUserWatchCode(), userEmail = {}, code = {}",userEmail,code);
        //TODO: 예외 처리 하자

        watchRepository.save(WatchRedis.builder().code(code).email(userEmail).build());
//        watchRedisManager.storeCode(userEmail,code);
        return WatchCode.builder()
                .watchCode(code).
                build();
    }




}
