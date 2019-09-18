package com.september.fuelup.confuration;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@EnableScheduling
public class CommonConfig {
	
	@Value("${rest.taxi.url}") private String url;
	@Value("${rest.taxi.park.url}") private String urlPark;
	@Value("${rest.taxi.api_key}") private String apiKey;
	
	@Bean
	public RestTemplateTaxi restTemplateTaxi() {
		int timeout = (int) TimeUnit.MINUTES.toMillis(1);
		
		HttpComponentsClientHttpRequestFactory httpRequestFactory =
				new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(timeout);
		httpRequestFactory.setConnectTimeout(timeout);
		httpRequestFactory.setReadTimeout(timeout);
		
		RestTemplateTaxi rest = new RestTemplateTaxi(httpRequestFactory);
		rest.setAPIKey(apiKey);
		rest.setUriTemplateHandler(new DefaultUriBuilderFactory(url));	
		return rest;
	}

	@Bean
	public RestTemplateTaxi restTemplateTaxiPark() {
		int timeout = (int) TimeUnit.MINUTES.toMillis(1);
		
		HttpComponentsClientHttpRequestFactory httpRequestFactory =
				new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(timeout);
		httpRequestFactory.setConnectTimeout(timeout);
		httpRequestFactory.setReadTimeout(timeout);
		
		RestTemplateTaxi rest = new RestTemplateTaxi(httpRequestFactory);
		rest.setAPIKey(apiKey);
		rest.setUriTemplateHandler(new DefaultUriBuilderFactory(urlPark));	
		return rest;
	}
}
