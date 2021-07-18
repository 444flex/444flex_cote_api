package com.flex.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AnswerSubmitResDto {

	@JsonProperty("answer_id")
	private Long id;
	
	@JsonProperty("submit_yn")
	private boolean submitYn;
}
