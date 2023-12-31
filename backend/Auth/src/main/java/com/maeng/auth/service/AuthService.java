package com.maeng.auth.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maeng.auth.dto.CodeDto;
import com.maeng.auth.dto.OAuthToken;
import com.maeng.auth.dto.RecordInfoDto;
import com.maeng.auth.dto.TokenInfoResponse;
import com.maeng.auth.dto.WatchToken;
import com.maeng.auth.entity.Fcm;
import com.maeng.auth.entity.User;
import com.maeng.auth.entity.WatchJwtRedis;
import com.maeng.auth.entity.WatchRedis;
import com.maeng.auth.exception.AuthException;
import com.maeng.auth.exception.ExceptionCode;
import com.maeng.auth.repository.FcmRepository;
import com.maeng.auth.repository.UserRepository;
import com.maeng.auth.repository.WatchRepository;
import com.maeng.auth.repository.WatchTokenRepository;
import com.maeng.auth.util.JwtProvider;
import com.maeng.auth.util.JwtRedisManager;

import java.util.UUID;

@Service
public class AuthService {
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final String clientId;

    private final String clientSecret;

    private final String accessTokenUrl;

    private final String userInfoUrl;

    private final UserRepository userRepository;

    private final JwtRedisManager jwtRedisManager;

    private final JwtProvider jwtProvider;

    private  final WatchRepository watchRepository;

    private final WatchTokenRepository watchTokenRepository;

    private final FcmRepository fcmRepository;
    public AuthService(RabbitTemplate rabbitTemplate, RestTemplate restTemplate,
                       @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                       @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret ,
                       @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String accessTokenUrl,
                       @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String userInfoUrl,
                       JwtRedisManager jwtRedisManager, JwtProvider jwtProvider, UserRepository userRepository, WatchRepository watchRepository
                        ,WatchTokenRepository watchTokenRepository, FcmRepository fcmRepository){
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.jwtRedisManager = jwtRedisManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.watchRepository = watchRepository;
        this.watchTokenRepository =watchTokenRepository;
        this.fcmRepository = fcmRepository;

    }
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 인가 코드를 받아 액세스 토큰을 발급 받고, 이를 통해 사용자의 정보를 조회해 JWT 생성 및 반환
     * @param codeDto 인가 코드
     * @return Map Access Token, Refresh Token
     * */
    public OAuthToken createTokens(CodeDto codeDto) {
        String accessToken = getAccessToken(codeDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        User user = extractUser(response.getBody());
        System.out.println(user.toString());

        String userEmail = user.getEmail();
        // 사용자가 처음 서비스를 이용하는 경우
        if (userRepository.findUserByEmail(userEmail).isEmpty()) {
            userRepository.save(user);
            rabbitTemplate.convertAndSend("user", "register."+userEmail, RecordInfoDto.builder()
                .email(userEmail)
                .nickname(user.getNickname())
                .build());
        }


        //JWT 생성 하고, Redis 저장소에 Token 저장하는 로직 추가 필요 (확인 필요!)
        OAuthToken oAuthToken = new OAuthToken(jwtProvider.generateAccessToken(userEmail),
                jwtProvider.generateRefreshToken(userEmail));
        jwtRedisManager.storeJwt(userEmail, oAuthToken.getRefreshToken());

        //Fcm token 저장
        user = userRepository.findUserByEmail(userEmail).orElseThrow(()
                -> new AuthException(ExceptionCode.USER_NOT_FOUND));
        Fcm fcm = fcmRepository.findByUser(user).orElse(Fcm.builder().user(user).build());
        fcm.setFcmToken(codeDto.getFcmToken());
        fcmRepository.save(fcm);

        return oAuthToken;
    }

    /**
     * 토큰으로부터 사용자 ID 추출 후 반환
     * */
    public TokenInfoResponse getUserEmail(String token) {
        return new TokenInfoResponse(jwtProvider.getUserId(token));
    }

    /**
     * Refresh Token을 받아 유효성을 검사하고, 유효한 경우는 Access Token과 Refresh Token을 재발급
     */
    public OAuthToken reGenerateAuthToken(HttpServletRequest request) {

        String refreshToken = jwtProvider.resolveRefreshToken(request);
        logger.info("reGenerateAuthToken(), refreshToken = {}",refreshToken);
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            logger.info("검증 실패");
            return null;
        }
        logger.info("검증 끝");

        String id = jwtProvider.getUserId(refreshToken);

        return new OAuthToken(jwtProvider.generateAccessToken(id), jwtProvider.generateRefreshToken(id));
    }
    public WatchToken getWatchToken(String code){

        WatchRedis watchRedis = watchRepository.findByCode(code).orElseThrow(()
                -> new AuthException(ExceptionCode.WATCH_CODE_FAILED));
        WatchToken watchToken = new WatchToken(
                jwtProvider.generateAccessToken(watchRedis.getEmail()), jwtProvider.generateRefreshToken(watchRedis.getEmail()));
        logger.info("getWatchToken(), email = {}, WatchAccessToken = {}, watchRefreshToken = {}",
                watchRedis.getEmail(),watchToken.getWatchAccessToken(), watchToken.getWatchRefreshToken());

        watchTokenRepository.save(WatchJwtRedis.builder()
                        .email(watchRedis.getEmail())
                        .watchAccessToken(watchToken.getWatchAccessToken())
                        .watchRefreshToken(watchToken.getWatchRefreshToken())
                .build());

        return watchToken;
    }

    public WatchToken regenerateWatchToken(WatchToken watchToken){
        String refreshToken = watchToken.getWatchRefreshToken();
        logger.info("regenerateWatchToken(), refreshToken = {}",refreshToken);
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            logger.info("검증 실패");
            return null;
        }
        String email = jwtProvider.getUserId(refreshToken);
        WatchToken newToken = new WatchToken(jwtProvider.generateAccessToken(email), jwtProvider.generateRefreshToken(email));
        watchTokenRepository.save(WatchJwtRedis.builder()
                        .email(email)
                        .watchAccessToken(newToken.getWatchAccessToken())
                        .watchRefreshToken(newToken.getWatchRefreshToken())
                .build());
        return newToken;
    }


    /**
     * 인가 코드를 통해 KAKAO로부터 액세스 토큰을 발급받아 반환
     * @param codeDto 인가 코드
     * @return String 액세스 토큰
     * */
    private String getAccessToken(CodeDto codeDto) {
        return extractAccessToken(requestAccessToken(codeDto).getBody());
    }

    /**
     * 주어진 정보로부터 User 객체를 생성해 반환
     * @param data
     * @return User
     * */
    private User extractUser(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);

            String email = jsonNode.get("kakao_account").get("email").asText();
            String profileImage = jsonNode.get("properties").get("profile_image").asText();
            String nickname = getNickname();
            return new User(email, nickname,profileImage);
        } catch (Exception e) {
            throw new AuthException(ExceptionCode.USER_CREATED_FAILED);
        }
    }
    public String getNickname() {
        String nickname = generateUniqueNickname();
        // 중복 체크
        while (userRepository.existsByNickname(nickname)) {
            nickname = generateUniqueNickname();
        }
        return nickname;
    }
    private String generateUniqueNickname() {
        UUID uuid = UUID.randomUUID();

        // UUID를 문자열로 변환하여 하이픈 제거
        String uuidString = uuid.toString().replaceAll("-", "");

        // 문자열에서 앞 6자리 가져오기
        return "player" + uuidString.substring(0, 6);
    }

    /**
     * 주어진 응답으로부터 Access Token을 추출해 반환
     * @param response 데이터
     * @return String Access Token 또는 null
     * */
    private String extractAccessToken(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException(ExceptionCode.KAKAO_TOKEN_RESPONSE_FAILED);
        }
    }
    /** 코드를 입력 받아 정보 확인 후 AccessToken 확인 */



    private ResponseEntity<String> requestAccessToken(CodeDto codeDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            return restTemplate.exchange(
                    accessTokenUrl + "?grant_type=authorization_code" +"&client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + codeDto.getCode() ,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        } catch (Exception e) {
            throw new AuthException(ExceptionCode.KAKAO_ACCESS_TOKEN_FETCH_FAILED);
        }
    }

    @Transactional(readOnly = true)
	public String getProfile(String nickname) {
        User user = userRepository.findUserByNickname(nickname).orElseThrow(()
                -> new AuthException(ExceptionCode.USER_NOT_FOUND));
        return user.getProfile_image();
	}
}
