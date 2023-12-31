package com.maeng.gateway.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    private final Environment env;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Key key;
    @Autowired
    public JwtAuthenticationFilter(Environment env, @Value("${jwt.secret-key}") String secretKey) {
        super(Config.class);
        this.env = env;
        logger.info("secretKey ={}" , secretKey);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public static class Config {

    }
    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            logger.info("JwtAuthenticationFilter()");
            ServerHttpRequest request = exchange.getRequest();
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                logger.info("header 없음");
                return onError(exchange, "No authoriazation header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            logger.info("jwt ={}", jwt);

            if(!isJwtValid(jwt)){
                logger.info("유효하지 않은 토큰");
                return onError(exchange, "JWT token is not valid",HttpStatus.UNAUTHORIZED);
            }
            String email = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
            logger.info("email ={} ", email);
//            exchange.mutate().request(builder -> builder.header("userEmail", email)).build();
            // 이메일 값을 헤더로 추가
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("userEmail", email)
                    .build();

//            return chain.filter(exchange);
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        });



    }

    private boolean isJwtValid(String jwt){
        logger.info("isJwtValid()");
        boolean returnValue = true;

        String subject = null;
        Date expire = null;

        try {
            subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
            expire  = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getExpiration();

            logger.info("subject = {}, expire ={} ", subject, expire);

        } catch (JwtException | IllegalArgumentException e) {
            returnValue = false;
        }

        if(subject == null || subject.isEmpty() || expire.before(new Date())){
            logger.info("만료..?");
            returnValue = false;
        }

        logger.info("returnValue = {}", returnValue);

        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response  =  exchange.getResponse();
        response.setStatusCode(httpStatus);
        logger.info("에러 발생");
        logger.error(err);
        return response.setComplete();

    }
}
