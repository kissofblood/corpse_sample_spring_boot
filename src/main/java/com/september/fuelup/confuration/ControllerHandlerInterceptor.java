package com.september.fuelup.confuration;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.september.fuelup.model.Provider;
import com.september.fuelup.repository.ProviderRepository;
import com.september.fuelup.util.Common;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ControllerHandlerInterceptor implements HandlerInterceptor {
	
	private ProviderRepository providerRepository;
	private String apiKey;

	public ControllerHandlerInterceptor(ProviderRepository providerRepository, String apiKey) {
		this.providerRepository = providerRepository;
		this.apiKey = apiKey;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        if(request.getMethod().equalsIgnoreCase("POST")) {
        	CachingRequestWrapper wrapper = (CachingRequestWrapper) request;
        	
        	String authSignature = request.getHeader(Common.KEY_AUTH_SIGNATURE);
        	String partnerId = request.getHeader(Common.KEY_PARTNER_ID);
        
        	if(authSignature == null) {
        		throw new RuntimeException("request " + Common.KEY_AUTH_SIGNATURE);
        	}
        	if(partnerId == null) {
        		throw new RuntimeException("request " + Common.KEY_PARTNER_ID);
        	}
        
        	Provider provider = providerRepository.getFirstByProviderId(partnerId).orElse(null);
        	if(provider == null) {
        		throw new RuntimeException(Common.KEY_PARTNER_ID + "[" + partnerId + "] not found");
        	}
            
        	String body = wrapper.getReader()
        			.lines()
        			.collect(Collectors.joining(System.lineSeparator()));
            String hex = Common.dataToHmacSHA1(apiKey.getBytes(), body.getBytes());
            if(!hex.equals(authSignature)) {
            	log.error(Common.KEY_AUTH_SIGNATURE + " not equal " + hex + " - " +
            		authSignature + " data " + body);
        		throw new RuntimeException(Common.KEY_AUTH_SIGNATURE + "[" + authSignature + "] not found");
            }
        }
		return true;
	}
}
