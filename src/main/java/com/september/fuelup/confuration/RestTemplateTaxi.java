package com.september.fuelup.confuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTaxi extends RestTemplate {

	public RestTemplateTaxi(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}
	
	private void changeInterceptor(String apiKey, String partnerID) {
		ClientHttpRequestInterceptorImpl interceptor = null;
		for(ClientHttpRequestInterceptor inter : getInterceptors()) {
			if(inter instanceof ClientHttpRequestInterceptorImpl) {
				interceptor = (ClientHttpRequestInterceptorImpl) inter;
			}
		}
		
		if(interceptor == null) {
			interceptor = new ClientHttpRequestInterceptorImpl();

			List<ClientHttpRequestInterceptor> interceptors =
				new ArrayList<ClientHttpRequestInterceptor>();
			interceptors.add(interceptor);
			setInterceptors(interceptors);
		}
		
		if(apiKey != null) {
			interceptor.setApiKey(apiKey);
		}
		if(partnerID != null) {
			interceptor.setPartnerID(partnerID);
		}
	}
	
	public void setAPIKey(String apiKey) {
		changeInterceptor(apiKey, null);
	}
	
	public void setPartnerID(String partnerID) {
		changeInterceptor(null, partnerID);
	}
}
