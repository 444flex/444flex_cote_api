package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class EntityNotFoundException extends ServiceException {

	static final HttpStatus RESPONSE_CODE = HttpStatus.NOT_FOUND;
	static final ErrorType ERROR_TYPE = ErrorType.ClientSystem;
	static final ErrorReason ERROR_REASON = ErrorReason.EntityNotFound;
	static final int ERROR_CODE = 4001;
	
	public EntityNotFoundException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}

}
