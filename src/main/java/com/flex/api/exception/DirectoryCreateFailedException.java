package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class DirectoryCreateFailedException extends ServerSideException {

	static final HttpStatus RESPONSE_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
	static final ErrorType ERROR_TYPE = ErrorType.ServerSystem;
	static final ErrorReason ERROR_REASON = ErrorReason.DirectoryCreateFailed;
	static final int ERROR_CODE = 5003;
	
	public DirectoryCreateFailedException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, ERROR_TYPE, ERROR_REASON, ERROR_CODE, errorTarget, errorMessage, cause);
	}
}
