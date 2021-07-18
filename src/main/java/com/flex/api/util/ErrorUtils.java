package com.flex.api.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.flex.api.exception.ClientRequestDataInvalidException;

public class ErrorUtils {
	
	public static void checkBindingResult(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			for (ObjectError error : bindingResult.getAllErrors())
				sb.append(error.getDefaultMessage()).append("\n");
			throw new ClientRequestDataInvalidException(bindingResult.getAllErrors().get(0).getObjectName(), sb.toString(), null);
		}
	}

}
