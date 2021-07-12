package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class EntityDuplicatedException extends ServiceException {

	static final HttpStatus RESPONSE_CODE = HttpStatus.CONFLICT;
	static final ErrorType ERROR_TYPE = ErrorType.ClientSystem;
	static final ErrorReason ERROR_REASON = ErrorReason.EntityDuplicated;
	static final int ERROR_CODE = 4002;
	
	public EntityDuplicatedException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}
}
