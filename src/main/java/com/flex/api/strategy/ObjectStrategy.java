package com.flex.api.strategy;

public interface ObjectStrategy {

	public Object getTypeAndValue(String value) throws ClassNotFoundException;
}
