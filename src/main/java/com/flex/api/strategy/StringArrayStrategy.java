package com.flex.api.strategy;

import java.lang.reflect.Array;
import java.util.Arrays;

public class StringArrayStrategy implements ObjectStrategy {
	
	private static final String TYPE = "java.lang.String";

	@Override
	public Object getTypeAndValue(String type, String value) throws ClassNotFoundException {
		Object[] copy = null;
		String[] valueArray = value.replace("{", "").replace("}", "").split(",");
		Class<?> cls = Class.forName(TYPE);
		Object array = Array.newInstance(cls, valueArray.length);
		Class<?> arrayType = array.getClass();
		copy = Arrays.copyOf((Object[])arrayType.cast(array), Array.getLength(array));
		for (int i=0; i<valueArray.length; i++) {
			copy[i] = valueArray[i];
		}
		return copy;
	}

}
