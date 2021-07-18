package com.flex.api.dto.request;


import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AnswerReqDto {

	@JsonProperty("question_id")
	private Long questionId;
	
	@NotEmpty(message = "Code is Empty")
	@JsonProperty("code")
	private String code;
	
	

}
