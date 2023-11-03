package com.maeng.game.global.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		log.info("Request: " + req.getRemoteAddr() + " " + req.getMethod() + " " + req.getRequestURI());

		chain.doFilter(request, response);

	}
}
