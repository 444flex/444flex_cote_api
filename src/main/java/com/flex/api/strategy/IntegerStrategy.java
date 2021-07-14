package com.flex.api.strategy;

public class IntegerStrategy implements ObjectStrategy {

	@Override
	public Object getTypeAndValue(String type, String value) {
		return Integer.parseInt(value);
	}

}
