package com.flex.api.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

	public enum ErrorType{
		UserInput,
		ClientSystem,
		ServerSystem
	}
	
	public enum ErrorReason {
		Invalid,
		Blank,
		NotMatched,
		
		EntityNotFound,
		EntityDuplicated,
		EntityNotModify,
		
		InternalServerError,
		FileCreateFailed,
		DirectoryCreateFailed,
		CompileError,
		RuntimeTimeout,
		FileNotFound,
		DirectoryNotFound
	}
	
	protected HttpStatus responseCode;
	protected ErrorType errorType;
	protected ErrorReason errorReason;
	
	protected String errorTarget;
	protected int errorCode;
	protected String errorMessage;
	
	public ServiceException(HttpStatus responseCode, ErrorType errorType, ErrorReason errorReason, int errorCode, String errorTarget, String errorMessage, Throwable cause) {
		super(cause);
		this.responseCode = responseCode;
		this.errorType = errorType;
		this.errorReason = errorReason;
		this.errorCode = errorCode;
		this.errorTarget = errorTarget;
		this.errorMessage = errorMessage;
	}
	
	public Map<String, Object> toServiceMessageMap() {
		Map<String, Object> messageMap = new LinkedHashMap<>();
		messageMap.put("reponseCode", this.responseCode.value());
		if (this.errorType != null) {
			messageMap.put("errorType", this.errorType);
		}
		if (this.errorTarget != null) {
			messageMap.put("errorTarget", this.errorTarget);
		}
		if (this.errorReason != null) {
			messageMap.put("errorReason", this.errorReason);
		}
		messageMap.put("errorCode", this.errorCode);
		if (this.errorMessage != null) {
			messageMap.put("errorMessage", this.errorMessage);
		}
		return messageMap;
	}
}
