package com.september.fuelup.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.september.fuelup.model.Bank;
import com.september.fuelup.model.BankProvider;
import com.september.fuelup.model.DayPaymentLimit;
import com.september.fuelup.model.Driver;
import com.september.fuelup.model.ServiceHasVariable;
import com.september.fuelup.model.rest.RespResult;
import com.september.fuelup.model.rest.RespStatus;
import com.september.fuelup.repository.BankProviderRepository;
import com.september.fuelup.repository.BankRepository;
import com.september.fuelup.repository.DayPaymentLimitRepository;
import com.september.fuelup.repository.DriverRepository;
import com.september.fuelup.repository.DriverToFuelUpRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DriverToFuelUpService {
	
	@Autowired private DriverToFuelUpRepository driverToFuelUpRepository;
	@Autowired private DriverRepository driverRepository;
	@Autowired private BankRepository bankRepository;
	@Autowired private DayPaymentLimitRepository dayPaymentLimitRepository;
	@Autowired private DriverService driverService;
	@PersistenceContext private EntityManager entityManager;
	@Autowired private BankProviderRepository bankProviderRepository;

	public RespResult holdMoney(Long driverId, String orderId, BigDecimal amount) {
		int count = driverToFuelUpRepository.updateHoldMoneyAndOrdersByDriverId(
			amount, orderId, driverId
		);
		RespResult respResult = new RespResult();
		if(count == 0) {
			respResult.setResult(RespResult.RESULT_DENY);
			respResult.setMessage("update count 0");
		}
		else {
			respResult.setResult(RespResult.RESULT_OK);
		}
		return respResult;
	}
	
	public RespResult finishOrder(Long driverId, String orderId, BigDecimal amount) {
		Driver driver = driverService.getDriverById(driverId);
		BigDecimal holdMoney = driverToFuelUpRepository.getFirstByDriverId(driverId)
			.get().getHoldMoney();
		if(amount.toBigInteger().compareTo(holdMoney.toBigInteger()) > 0) {
			log.error("amount.compareTo(holdMoney) > 0 -> amount " +
				amount + " holdMoney " + holdMoney + " -> " + driver);
			throw new RuntimeException("amount > holdmoney");
		}
		
		if(amount.compareTo(BigDecimal.ZERO) == 0) {
			driverToFuelUpRepository.updateHoldMoneyAndOrdersByDriverId(
				BigDecimal.ZERO, null, driverId
			);
			log.debug("amount.compareTo(BigDecimal.ZERO) == 0 -> " + driver);
		}
		else {
			Driver dr = driverRepository.findById(driverId).get();
			Bank bank = new Bank();
			bank.setDriverId(driverId);
			bank.setSum(amount);
			bank.setDtArrive(Calendar.getInstance());
			bank.setDescription("заправка FuelUp № " + orderId);
			bank.setServiceID(dr.getServiceId());
			bank.setTrStatus(0);
			bank.setTrType(17);
			bank.setFuelCardTransactionID(null);
			bankRepository.save(bank);
			
			List<ServiceHasVariable> serviceHasVariables = entityManager.createQuery(
				"FROM ServiceHasVariable WHERE variableId=:variableId and serviceId in ( " +
					"SELECT serviceId FROM Driver WHERE id=:id)", ServiceHasVariable.class
			)
			.setParameter("variableId", 194)
			.setParameter("id", driver.getId())
			.getResultList();
			if(!serviceHasVariables.isEmpty()) {
				ServiceHasVariable serviceHasVariable = serviceHasVariables.get(0);
				log.debug(serviceHasVariable);
				if(!serviceHasVariable.getValue().equals("1")) {
					BankProvider bankProvider = new BankProvider();
					bankProvider.setDriverId(driver.getId());
					bankProvider.setSum(amount.negate());
					bankProvider.setDtArrive(Calendar.getInstance());
					bankProvider.setDescription("заправка FuelUp № " + orderId);
					bankProvider.setServiceID(dr.getServiceId());
					bankProvider.setTrStatus(0);
					bankProvider.setTrType(20);
					bankProvider.setHandledByTCT(0);
					bankProviderRepository.save(bankProvider);
					
					log.debug("INSERT " + bankProvider);
				}
			}
			
			log.debug("insert bank -> " + driver);
			
			driverToFuelUpRepository.updateHoldMoneyAndOrdersByDriverId(
				BigDecimal.ZERO, null, driverId
			);

			BigDecimal lim = dayPaymentLimitRepository.getFirstByDriverId(driverId)
				.map(DayPaymentLimit::getFuelLimit)
				.orElse(null);
			BigDecimal lim1 = new BigDecimal(
				driverService.getServiceHasVariable172(driverId).get().getValue()
			);
			
			if(lim1.compareTo(BigDecimal.ZERO) != 0 && lim == null) {
				DayPaymentLimit dayPaymentLimit = new DayPaymentLimit();
				dayPaymentLimit.setDate(new Date());
				dayPaymentLimit.setDriverId(driverId);
				dayPaymentLimit.setFuelLimit(lim1.subtract(amount));
				dayPaymentLimitRepository.save(dayPaymentLimit);
				
				log.debug("lim1.compareTo(BigDecimal.ZERO) != 0 && lim == null -> " +
					"lim1 " + lim1 + " lim " + lim + " fuelLimit " +
						dayPaymentLimit.getFuelLimit() + " amount " + amount + 
						" -> " + driver);
			}
			else if(lim.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal fuelLimit = lim.subtract(amount);
				dayPaymentLimitRepository.updateFuelLimitByDriverId(fuelLimit, driverId);
				log.debug("lim.compareTo(BigDecimal.ZERO) > 0 -> lim " + lim +
					" fuelLimit " + fuelLimit + " amount " + amount + " " + driver);
			}
		}
		
		RespResult respResult = new RespResult();
		respResult.setResult(RespStatus.STATUS_OK);
		return respResult;
	}
}
