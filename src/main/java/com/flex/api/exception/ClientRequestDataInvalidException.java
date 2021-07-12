package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ClientRequestDataInvalidException extends ServiceException {

	static final HttpStatus RESPONSE_CODE = HttpStatus.BAD_REQUEST;
	static final ErrorType ERROR_TYPE = ErrorType.UserInput;
	static final ErrorReason ERROR_REASON = ErrorReason.Invalid;
	static final int ERROR_CODE = 3001;
	
	public ClientRequestDataInvalidException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}
}
