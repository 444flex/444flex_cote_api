package com.flex.api.strategy;

public class IntegerMultiArrayStrategy implements ObjectStrategy {

	@Override
	public Object getTypeAndValue(String value) throws ClassNotFoundException {
		int count = 0;
		for (char c : value.toCharArray()) {
			if (c == '{' || c == ',') {
				continue;
			} else if (c == '}') {
				break;
			} else {
				count++;
			}
		}
		int[][] result = new int[count][count];
		
		
		String[] valueArray = value.replace("{", "").replace("}", "").split(",");
		
		int i=0;
		for (int[] innerArray : result) {
			for (int j=0; j<innerArray.length; j++) {
				innerArray[j] = Integer.parseInt(valueArray[i]);
				i++;
			}
		}
		
		return result;
	}

}
