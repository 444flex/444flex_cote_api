package com.flex.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import lombok.extern.slf4j.Slf4j;

@WebFilter(urlPatterns="/flex/*")
@Slf4j
public class HttpFilter implements Filter{
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		Filter.super.init(filterConfig);
		log.info("#### init filter #####");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("#### filter - before ####");
		chain.doFilter(request, response);
		log.info("#### filter - after ####");
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() {
		log.info("#### destroy filter #####");
	}

}
