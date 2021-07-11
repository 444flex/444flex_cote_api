package com.flex.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AnswerReqDto {

	@JsonProperty("question_id")
	private Long questionId;
	
	@JsonProperty("code")
	private String code;

}
