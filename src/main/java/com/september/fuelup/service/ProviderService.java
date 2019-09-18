package com.september.fuelup.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.september.fuelup.confuration.RestTemplateTaxi;
import com.september.fuelup.model.Provider;
import com.september.fuelup.repository.ProviderRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProviderService {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired private ProviderRepository providerRepository;
	@Autowired private RestTemplateTaxi restTemplateTaxiPark;
	@Value("${rest.taxi.partner_id}") private String partnerId;

	public void createPark() throws JsonParseException, JsonMappingException, IOException {
		for(Provider provider : providerRepository.getByTypeAndProviderId(7, "New")) {
			Map<String, String> map = new HashMap<>();
			if(provider.getName() != null) {
				map.put("name", provider.getName());
			}
			if(provider.getEmail() != null) {
				map.put("email", provider.getEmail());
			}
			if(provider.getPhone() != null) {
				map.put("phone", provider.getPhone());
			}
			if(provider.getINN() != null) {
				map.put("inn", provider.getINN());
			}
			if(provider.getCompanyName() != null) {
				map.put("official_name", provider.getCompanyName());
			}
			if(provider.getContactName() != null) {
				map.put("contact", provider.getContactName());
			}
			
			if(map.isEmpty()) {
				log.error("map is empty -> " + provider);
				continue;
			}
			
			restTemplateTaxiPark.setPartnerID(partnerId);
			String respStatus = restTemplateTaxiPark.postForObject("/ta/create_park", map, String.class);
			log.debug("raw respStatus " + respStatus);
			
			String st = mapper.readValue(respStatus, String.class);
			int count = providerRepository.updateProviderIdById(st, provider.getId());
			log.info("/createPark " + provider + " " + st + " -> " + count);
		}
	}
}
