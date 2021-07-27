package com.flex.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public class UserResDto {
	
	@JsonProperty("user_id")
	private Long userId;
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("expires_in")
	private Long expiresIn;
	
	
}
