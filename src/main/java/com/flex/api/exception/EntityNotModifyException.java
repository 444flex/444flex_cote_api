package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class EntityNotModifyException extends ServiceException {
	
	static final HttpStatus RESPONSE_CODE = HttpStatus.UNPROCESSABLE_ENTITY;
	static final ErrorType ERROR_TYPE = ErrorType.ClientSystem;
	static final ErrorReason ERROR_REASON = ErrorReason.EntityNotModify;
	static final int ERROR_CODE = 4003;
	
	public EntityNotModifyException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}
}
