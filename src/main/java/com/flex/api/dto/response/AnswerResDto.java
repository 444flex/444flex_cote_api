package com.flex.api.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AnswerResDto {

	public AnswerResDto() {
		this.testCaseList = new ArrayList<AnswerResDto.TestCase>();
	}
	
	@JsonProperty("score")
	private Integer Score;
	
	@JsonProperty("test_cast_list")
	private List<TestCase> testCaseList;
	
	@Data
	@AllArgsConstructor
	public static class TestCase {
		private boolean compileYn;
		
		private Long compileTime;
	}
	
	public void setTestCase(boolean compileYn, Long compileTime) {
		this.testCaseList.add(new TestCase(compileYn, compileTime));
	}
}
