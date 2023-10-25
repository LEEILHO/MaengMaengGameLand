package com.maeng.auth.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    /* Auth */
    USER_CREATED_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 객체 생성에 실패했습니다."),
    KAKAO_ACCESS_TOKEN_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다."),
    KAKAO_TOKEN_RESPONSE_FAILED(HttpStatus.BAD_GATEWAY, "KAKAO 토큰 정보 추출에 실패했습니다.");
    private final HttpStatus status;
    private final String message;

}
