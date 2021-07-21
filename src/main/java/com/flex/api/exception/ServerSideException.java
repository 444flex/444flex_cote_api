package com.flex.api.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ServerSideException extends ServiceException {

	static public final HttpStatus RESPONSE_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
	static public final ErrorType DEFAULT_ERROR_TYPE = ServiceException.ErrorType.ServerSystem;
	static public final ErrorReason DEFAULT_ERROR_REASON = ServiceException.ErrorReason.InternalServerError;
	
	static public final int DEFAULT_ERROR_CODE = 5001;
	static public final String DEFAULT_CLIENT_MESSAGE = "Internal Server Error";
	
	public ServerSideException(HttpStatus responseCode, int errorCode, String errorTarget, String errorMessage, Throwable cause) {
		super(responseCode, DEFAULT_ERROR_TYPE, DEFAULT_ERROR_REASON, errorCode, errorTarget, errorMessage, cause);
	}
	
	public ServerSideException(HttpStatus responseCode, ErrorType errorType, ErrorReason errorReason, int errorCode, String errorTarget, String errorMessage, Throwable cause) {
		super(responseCode, errorType, errorReason, errorCode, errorTarget, errorMessage, cause);
	}
	
	public ServerSideException(String errorTarget, String errorMessage, Throwable cause) {
		super(RESPONSE_CODE, DEFAULT_ERROR_TYPE, DEFAULT_ERROR_REASON, DEFAULT_ERROR_CODE, errorTarget, errorMessage, cause);
	}

}
