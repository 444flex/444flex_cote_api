package com.flex.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class QuestionResDto {

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("content")
	private String content;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("limit_time")
	private Integer limitTime;
	
}
