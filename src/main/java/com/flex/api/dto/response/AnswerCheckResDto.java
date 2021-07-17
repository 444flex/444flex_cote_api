package com.flex.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerCheckResDto {
	@JsonProperty("isSubmit")
	private Boolean isSubmit;

}
