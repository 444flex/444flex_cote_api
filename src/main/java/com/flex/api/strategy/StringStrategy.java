package com.flex.api.strategy;

public class StringStrategy implements ObjectStrategy {

	@Override
	public Object getTypeAndValue(String type, String value) {
		return value;
	}

}
