package com.flex.api.model;

import lombok.Data;

@Data
public class Result {

	private boolean isCorrect;
	private long timemils;
	private Object answer;
}
