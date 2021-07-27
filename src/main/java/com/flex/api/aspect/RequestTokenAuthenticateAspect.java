package com.flex.api.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flex.api.util.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Aspect
@Component
public class RequestTokenAuthenticateAspect {

	@Value("${flex.auth.token.secretkey:testsecretkey}")
	private String secretKey;
	
	
	@Before("@annotation(com.flex.api.annotation.RequiredToken)")
	public void beforeComponentMethod(JoinPoint jp) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String token = null;
		
		token = request.getHeader("token");
		if (StringUtils.hasLength(token)) {
			this.authenticate(token);
		} else {
			throw new ServiceException("Invalid request");
		}
	}
	
	// TODO userID 검증로직 추가 및 exception 정의
	private void authenticate(String token) {
		try {
			Claims claims = JwtUtils.getTokenClaims(token, secretKey);
			Long userId = Long.parseLong(claims.get("id").toString());
		} catch (ExpiredJwtException e) {
			throw new ServiceException("Token is expired");
		} catch (Exception e) {
			throw new ServiceException("Invalid client");
		}
	}
}
