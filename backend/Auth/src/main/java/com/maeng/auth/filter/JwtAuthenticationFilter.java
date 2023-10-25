package com.maeng.auth.filter;

import com.maeng.auth.util.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // oauth, swagger ui에 대한 URL은 필터 처리 X
//        if (requestURI.startsWith("/api/v1/auth") || requestURI.startsWith("/favicon.ico")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

         if (requestURI.startsWith("/")) {
         	filterChain.doFilter(request, response);
         	return;
         }

        String token = jwtProvider.resolveToken(request);

        // JWT가 유효한 경우, 즉 사용자 인증이 완료된 상황
        // 사용자 ID를 추출하여 Authentication에 저장
        if (token != null && jwtProvider.validateToken(token)) {
            String userId = jwtProvider.getUserId(token);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        logger.info("[Filter] Token 존재하지 않거나 만료");

        // 유효하지 않은 경우는 401 에러 전달
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
