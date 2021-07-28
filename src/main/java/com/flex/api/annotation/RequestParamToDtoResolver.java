package com.flex.api.annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestParamToDtoResolver implements HandlerMethodArgumentResolver {

	private final ObjectMapper mapper;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(RequestParamToDto.class) != null; 
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		final String json = this.queryToJson(request.getQueryString());
		final Object object = this.mapper.readValue(json, parameter.getParameterType());
		return object;
	}
	
	private String queryToJson(String a) {
		final int length = a.length();
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
        for (int i = 0; i < length; i++) {
            if (a.charAt(i) == '=') {
            	sb.append("\"" + ":" + "\"");
            } else if (a.charAt(i) == '&') {
            	sb.append("\"" + "," + "\"");
            } else {
            	sb.append(a.charAt(i));
            }
        }
        sb.append("\"" + "}");
        return sb.toString();
	}
}
