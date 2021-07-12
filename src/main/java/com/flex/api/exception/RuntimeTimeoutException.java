package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class RuntimeTimeoutException extends ServerSideException {

	static final HttpStatus RESPONSE_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
	static final ErrorType ERROR_TYPE = ErrorType.ServerSystem;
	static final ErrorReason ERROR_REASON = ErrorReason.RuntimeTimeout;
	static final int ERROR_CODE = 5005;
	
	public RuntimeTimeoutException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}
}
