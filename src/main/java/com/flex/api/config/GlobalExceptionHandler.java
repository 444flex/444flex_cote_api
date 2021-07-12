package com.flex.api.config;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flex.api.exception.ServerSideException;
import com.flex.api.exception.ServiceException;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException e) throws Exception {
		// 로깅해야댐.. 나중에 elk 스택 해봅시당.
		
		return new ResponseEntity<Map<String, Object>>(e.toServiceMessageMap(), e.getResponseCode());
	}
	
	@ExceptionHandler(ServerSideException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleServerSideException(ServerSideException e) throws Exception {
		// 로깅해야댐..
		return new ResponseEntity<Map<String, Object>>(e.toServiceMessageMap(), e.getResponseCode());
	}
}
