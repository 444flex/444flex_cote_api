package com.flex.api.strategy;

public interface ObjectStrategy {

	public Object getTypeAndValue(String type, String value) throws ClassNotFoundException;
}
