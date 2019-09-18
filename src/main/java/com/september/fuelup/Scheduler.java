package com.september.fuelup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.september.fuelup.service.DayPaymentLimitService;
import com.september.fuelup.service.DriverService;
import com.september.fuelup.service.ProviderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class Scheduler {
	
	@Autowired private DriverService driverService;
	@Autowired private DayPaymentLimitService dayPaymentLimitService;
	@Autowired private TransactionTemplate transactionTemplate;
	@Autowired private ProviderService providerService;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void dropFuelLimit() {
		log.info("run dropFuelLimit");
		dayPaymentLimitService.dropFuelLimit();
	}
	
	@Scheduled(cron = "0 */5 * * * *")
	public void run() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("run addDriver");
					driverService.add();
				}
				catch(Exception e) {
					status.setRollbackOnly();
					log.error("addDriver", e);
				}
			}
		});
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("run delDriver");
					driverService.del();
				}
				catch(Exception e) {
					status.setRollbackOnly();
					log.error("delDriver", e);
				}
			}
		});
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("run createPark");
					providerService.createPark();
				}
				catch(Exception e) {
					status.setRollbackOnly();
					log.error("createPark", e);
				}
			}
		});
	}
	
	//@Scheduled(cron = "0 */15 * * * *")
	/*
	public void driver() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("run addDriver");
					driverService.add();
				}
				catch(Exception e) {
					status.setRollbackOnly();
					log.error("addDriver", e);
				}
			}
		});
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					log.info("run delDriver");
					driverService.del();
				}
				catch(Exception e) {
					status.setRollbackOnly();
					log.error("delDriver", e);
				}
			}
		});
	}
	
	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void createPark() throws JsonParseException, JsonMappingException, IOException {
		log.info("run createPark");
		providerService.createPark();
	}
	*/
}
