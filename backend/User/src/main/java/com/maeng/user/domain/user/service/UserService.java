package com.maeng.user.domain.user.service;

//import com.maeng.user.config.WatchRedisManager;

import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.maeng.user.domain.score.entity.Score;
import com.maeng.user.domain.score.enums.Tier;
import com.maeng.user.domain.score.repository.ScoreRepository;
import com.maeng.user.domain.user.dto.UserDetailResponse;
import com.maeng.user.domain.user.dto.UserNicknameEditDTO;
import com.maeng.user.domain.user.dto.WatchCode;
import com.maeng.user.domain.user.entity.User;
import com.maeng.user.domain.user.entity.WatchRedis;
import com.maeng.user.domain.user.exception.UserException;
import com.maeng.user.domain.user.exception.UserExceptionCode;
import com.maeng.user.domain.user.respository.UserRepository;
import com.maeng.user.domain.user.respository.WatchRepository;
import com.maeng.user.global.service.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
   private final ScoreRepository scoreRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final WatchRedisManager watchRedisManager;
    private final WatchRepository watchRepository;

    private final S3Uploader s3Uploader;

    private final RabbitTemplate rabbitTemplate;

    public UserDetailResponse getUserDetail(String userEmail){
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
        Score score = scoreRepository.findByUser(user).orElse(Score.builder()
            .score(0)
            .tier(Tier.BRONZE)
            .win(0)
            .lose(0)
            .build());

        /*더미 데이터로*/
        return UserDetailResponse.builder()
                .userEmail(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfileImage())
                .tier(score.getTier())
                .score(score.getScore())
                .win(score.getWin())
                .lose(score.getLose())
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

    @Transactional
    public User getUserByEmail(String email) {
        logger.info("getUserByEmail(), email = {}", email);
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
    }

    public void editNickname(String userEmail, String nickname) {
        if(nickname.length() > 12) {
            logger.info("NICKNAME_LENGTH_EXCEED");
            throw new UserException(UserExceptionCode.NICKNAME_LENGTH_EXCEED);
        }

        userRepository.existsByNickname(nickname)
            .ifPresent(exists -> {
                logger.info("NICKNAME_ALREADY_EXISTS");
                throw new UserException(UserExceptionCode.NICKNAME_ALREADY_EXISTS);
            });

        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        user.setNickname(nickname);

        rabbitTemplate.convertAndSend("user", "edit."+nickname, UserNicknameEditDTO.builder()
            .email(userEmail)
            .nickname(nickname)
            .build());

        userRepository.save(user);
    }

    public void editProfile(String userEmail, MultipartFile profileImage) {
        try {
            User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));
            logger.info("profileImage = {}", profileImage);
            String url = s3Uploader.upload(profileImage, "profile");

            String PATTERN = "https://maengland.s3.ap-northeast-2.amazonaws.com/";

            if(user.getProfileImage().contains(PATTERN)) {
                s3Uploader.deleteFile(user.getProfileImage().replace(PATTERN, ""));
            }

            user.setProfileImage(url);

            userRepository.save(user);
        } catch (IOException e) {
            throw new UserException(UserExceptionCode.FAIL_TO_EDIT_PROFILE);
        }
    }
}
