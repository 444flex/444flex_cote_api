package com.flex.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserReqDto {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("cell_number")
	private String cellNumber;

}
