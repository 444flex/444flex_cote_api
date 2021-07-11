package com.flex.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AnswerReqDto {

	@JsonProperty("user_id")
	private Long userId;
	
	@JsonProperty("question_id")
	private Long questionId;
	
	@JsonProperty("code")
	private String code;

}
