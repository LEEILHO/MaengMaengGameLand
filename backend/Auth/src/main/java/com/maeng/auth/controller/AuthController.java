package com.maeng.auth.controller;

import com.maeng.auth.dto.AuthAccessTokenResponse;
import com.maeng.auth.dto.CodeDto;
import com.maeng.auth.dto.OAuthToken;
import com.maeng.auth.service.AuthService;
import com.maeng.auth.util.CookieManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final AuthService authService;



    /**
     * 인가 코드를 받아 사용자 여부를 확인
     * 사용자가 아닌 경우, DB에 email을 id로 저장
     * 이후 github_id를 가지고 JWT 생성 후 반환
     * Access Token : AccessTokenResponse
     * Refresh Token : httpOnly Cookie
     * */
    @PostMapping("/kakao")
    public ResponseEntity<?> getCode(@RequestBody CodeDto codeDto, HttpServletResponse response) {
        logger.info("getCode(), code = {}", codeDto.getCode());

        OAuthToken oAuthToken = authService.createTokens(codeDto);

        response.addHeader("Set-Cookie", CookieManager.createCookie(oAuthToken.getRefreshToken()).toString());

        return ResponseEntity.ok().body(new AuthAccessTokenResponse(oAuthToken.getAccessToken()));
    }

    /**
     * 사용자의 Refresh Token을 받아 OAuthToken 재발급
     * 유효한 경우, 재발급
     * Access Token : AccessTokenResponse
     * Refresh Token : httpOnly Cookie
     * */
    @GetMapping("/token")
    public ResponseEntity<?> getToken(HttpServletRequest request, HttpServletResponse response) {
        logger.info("getToken(), Token 재발급");

        OAuthToken oAuthToken = authService.reGenerateAuthToken(request);

        if (oAuthToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        response.addHeader("Set-Cookie", CookieManager.createCookie(oAuthToken.getRefreshToken()).toString());

        return ResponseEntity.ok().body(new AuthAccessTokenResponse(oAuthToken.getAccessToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserId(@CookieValue(value = "refreshToken") String token) {
        logger.info("getUserId()");
        return ResponseEntity.ok().body(authService.getUserEmail(token));
    }
    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        logger.info("test");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/watch")
    public ResponseEntity<?> getWatchToken(){
        logger.info("getWatchToken()");
        return ResponseEntity.ok().build();

    }


}
