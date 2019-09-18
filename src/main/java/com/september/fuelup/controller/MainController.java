package com.september.fuelup.controller;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.september.fuelup.model.rest.DriverBalance;
import com.september.fuelup.model.rest.DriverInfo;
import com.september.fuelup.model.rest.RespResult;
import com.september.fuelup.service.DriverService;
import com.september.fuelup.service.DriverToFuelUpService;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@RestController
public class MainController {
	
	@ToString
	public static class Param {
		public String driver_id;	
		public String order_id;
		
		@Min(0)
		public BigDecimal amount;
	}

	@Autowired DriverService driverService;
	@Autowired DriverToFuelUpService driverToFuelUpService;
	
	@PostMapping("/get_driver_info")
	public DriverInfo getDriverInfo(@RequestBody Param param) {
		return driverService.getInfo(Long.valueOf(param.driver_id));
	}
	
	@PostMapping("/get_balance")
	public DriverBalance getDriverBalance(@RequestBody Param param) {
		return driverService.getBalance(Long.valueOf(param.driver_id));
	}
	
	@PostMapping("/hold_money")
	public RespResult holdMoney(@RequestBody @Valid Param param) {
		return driverToFuelUpService.holdMoney(
			Long.valueOf(param.driver_id), param.order_id, param.amount
		);
	}
	
	@PostMapping("/finish_order")
	public RespResult finishOrder(@RequestBody @Valid Param param) {
		log.debug("finishOrder -> " + param);
		return driverToFuelUpService.finishOrder(
			Long.valueOf(param.driver_id), param.order_id, param.amount
		);
	}
}
