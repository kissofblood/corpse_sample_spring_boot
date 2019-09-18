package com.september.fuelup.confuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CachingRequestBodyFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest currentRequest = (HttpServletRequest) request;
		CachingRequestWrapper wrapper = new CachingRequestWrapper(currentRequest);
		chain.doFilter(wrapper, response);
	}
}
