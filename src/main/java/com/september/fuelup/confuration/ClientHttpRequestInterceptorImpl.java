package com.september.fuelup.confuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.september.fuelup.util.Common;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor {
	
	private String partnerID;
	private String apiKey;
	
	public void setPartnerID(String partnerID) {
		this.partnerID = partnerID;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		if(body.length == 0) {
            return execution.execute(request, body);
		}
		
		try {
            request.getHeaders().add(Common.KEY_PARTNER_ID, partnerID);
            request.getHeaders().add(
            	Common.KEY_AUTH_SIGNATURE,
            	Common.dataToHmacSHA1(apiKey.getBytes(), body)
            );
            return execution.execute(request, body);
		}
		catch(NoSuchAlgorithmException | InvalidKeyException e) {
			log.error("failed authSignature", e);
			throw new IOException(e);
		}
	}
}
