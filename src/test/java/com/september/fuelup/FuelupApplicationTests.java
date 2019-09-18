package com.september.fuelup;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.september.fuelup.model.Driver;
import com.september.fuelup.repository.DriverToFuelUpRepository;
import com.september.fuelup.service.DayPaymentLimitService;
import com.september.fuelup.service.DriverService;
import com.september.fuelup.service.DriverToFuelUpService;
import com.september.fuelup.service.ProviderService;
import com.september.fuelup.util.Common;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FuelupApplicationTests {

	@Autowired private DriverService driverService;
	@Autowired private DayPaymentLimitService dayPaymentLimitService;
	@Autowired private ProviderService providerService;
	@Autowired private DriverToFuelUpRepository driverToFuelUpRepository;
	@Autowired private DriverToFuelUpService driverToFuelUpService;

	@Test
	public void testSign() throws NoSuchAlgorithmException, InvalidKeyException {
		String api = "5mpt8j/WWLqCdzA8SIK6efAIUEaJkk9cQ6EKLL1XpfwbqIliA03He4cbZKdFcBzA";
		String body = "{\"id\":\"test_user\", \"phone\": \"79001234567\"}";
		assertEquals(Common.dataToHmacSHA1(api.getBytes(), body.getBytes()), "LZZ1JrVIY9d/xR2+tsEJ1Kgg4r4=");
	}
	
	/*
	@Test
	@Transactional
	public void testAdd() {
		driverService.add();
	}
	
	@Test
	@Transactional
	public void testDel() {
		driverService.del();
	}
	
	@Test
	@Transactional
	public void testDrop() {
		dayPaymentLimitService.dropFuelLimit();
	}
	*/

	/*
	@Test
	@Transactional
	public void test() {
		driverToFuelUpRepository.updateChangedByDriverId(0, 3232l);
		int count = driverToFuelUpRepository.deleteByDriverId(5457l);
	}
	
	@Test
	@Transactional
	public void testCreatePark() throws JsonParseException, JsonMappingException, IOException {
		providerService.createPark();
	}
	
	@Test
	@Transactional
	public void delAll() {
		dayPaymentLimitService.dropFuelLimit();
	}
	
	@Test
	@Transactional
	public void testFinishOrder() {
		driverToFuelUpService.finishOrder(5597l, "test tst", new BigDecimal("1"));
	}
	*/
}
