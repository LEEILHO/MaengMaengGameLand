package com.maeng.game.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // STOMP 메세지 처리 방법 구성

    @Value("${spring.rabbitmq.host}")
    private String HOST;
    @Value("${spring.rabbitmq.username}")
    private String USERNAME;
    @Value("${spring.rabbitmq.password}")
    private String PASSWORD;

    // 메세지를 라우팅하는 메세지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));
        //registry.enableSimpleBroker("/queue", "/exchange", "/topic"); // 해당 주소를 구독하는 클라이언트에게 메세지 전송
        registry.setApplicationDestinationPrefixes("/pub"); // 메세지의 목적지 prefix 설정 : /pub으로 시작하는 요청만 메세지 브로커가 받음
        registry.enableStompBrokerRelay("/queue", "/exchange", "/topic")
                .setRelayPort(61613)
                .setRelayHost(HOST)
                .setSystemLogin(USERNAME)
                .setSystemPasscode(PASSWORD)
                .setClientLogin(USERNAME)
                .setClientPasscode(PASSWORD);
    }

    // 메세지를 전송할 endPoint 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/maeng")
                .setAllowedOriginPatterns("*").withSockJS();
                // 클라이언트에서 WebSocket에 접근할 수 있는 endPoint 지정 : ws://k9d208.p.ssafy.io:8080/maeng
                // setAllowOriginPatterns : CORS 설정
    }
}
