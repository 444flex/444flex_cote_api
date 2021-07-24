package com.flex.api.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserResDto {
	
	@JsonProperty("user_id")
	private Long userId;
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("iat")
	private Date iat;
	
	@JsonProperty("exp")
	private Date exp;
}
