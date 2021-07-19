package com.flex.api.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flex.api.exception.ServerSideException;
import com.flex.api.exception.ServiceException;
import com.flex.api.util.SlackUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

	private final SlackUtil slackUtil;
	
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException e) throws Exception {
		// 로깅해야댐.. 나중에 elk 스택 해봅시당.
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String message = makeMessage(request.getMethod(), request.getRequestURI(), e.toServiceMessageMap().get("errorCode").toString(), e.toServiceMessageMap().get("errorMessage").toString());
		slackUtil.sendErrorMessage(message);
		return new ResponseEntity<Map<String, Object>>(e.toServiceMessageMap(), e.getResponseCode());
	}
	
	@ExceptionHandler(ServerSideException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleServerSideException(ServerSideException e) throws Exception {
		// 로깅해야댐..
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String message = makeMessage(request.getMethod(), request.getRequestURI(), e.toServiceMessageMap().get("errorCode").toString(), e.toServiceMessageMap().get("errorMessage").toString());
		slackUtil.sendErrorMessage(message);
		return new ResponseEntity<Map<String, Object>>(e.toServiceMessageMap(), e.getResponseCode());
	}
	
	private String makeMessage(String method, String uri, String errorCode, String message) {
		return String.format("(%s) %s api's %s error : \n ```%s```", method, uri, errorCode, message);
	}
}
