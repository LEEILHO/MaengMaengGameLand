package com.maeng.auth.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maeng.auth.dto.CodeDto;
import com.maeng.auth.dto.OAuthToken;
import com.maeng.auth.dto.TokenInfoResponse;
import com.maeng.auth.entity.User;
import com.maeng.auth.exception.AuthException;
import com.maeng.auth.exception.ExceptionCode;
import com.maeng.auth.repository.UserRepository;
import com.maeng.auth.util.JwtProvider;
import com.maeng.auth.util.JwtRedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final String clientId;

    private final String clientSecret;

    private final String accessTokenUrl;

    private final String userInfoUrl;

    private final UserRepository userRepository;

    private final JwtRedisManager jwtRedisManager;

    private final JwtProvider jwtProvider;

    public AuthService(RestTemplate restTemplate,
                       @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                       @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret ,
                       @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String accessTokenUrl,
                       @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String userInfoUrl,
                       JwtRedisManager jwtRedisManager, JwtProvider jwtProvider, UserRepository userRepository){
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.jwtRedisManager = jwtRedisManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;

    }

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
        }


        //JWT 생성 하고, Redis 저장소에 Token 저장하는 로직 추가 필요 (확인 필요!)
        OAuthToken oAuthToken = new OAuthToken(jwtProvider.generateAccessToken(userEmail),
                jwtProvider.generateRefreshToken(userEmail));
        jwtRedisManager.storeJwt(userEmail, oAuthToken.getRefreshToken());

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
        String refreshToken = jwtProvider.resolveToken(request);

        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            return null;
        }

        String id = jwtProvider.getUserId(refreshToken);

        return new OAuthToken(jwtProvider.generateAccessToken(id), jwtProvider.generateRefreshToken(id));
    }




    /**
     * 인가 코드를 통해 Naver로부터 액세스 토큰을 발급받아 반환
     * @param codeDto 인가 코드, state
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
            String nickname = jsonNode.get("properties").get("nickname").asText();
            String profileImage = jsonNode.get("properties").get("profile_image").asText();
            User user = new User(email, email,profileImage);

            return user;
        } catch (Exception e) {
            throw new AuthException(ExceptionCode.USER_CREATED_FAILED);
        }
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




}
