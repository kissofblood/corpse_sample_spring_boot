package com.september.fuelup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.september.fuelup.repository.DayPaymentLimitRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DayPaymentLimitService {

	@Autowired private DayPaymentLimitRepository dayPaymentLimitRepository;

	public void dropFuelLimit() {
		int count = dayPaymentLimitRepository.allDelete();
		log.info("deleteAll dayPaymentLimit " + count);
	}
}
