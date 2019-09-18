package com.september.fuelup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.september.fuelup.confuration.RestTemplateTaxi;
import com.september.fuelup.model.DayPaymentLimit;
import com.september.fuelup.model.Driver;
import com.september.fuelup.model.DriversGroup;
import com.september.fuelup.model.Provider;
import com.september.fuelup.model.ServiceHasVariable;
import com.september.fuelup.model.TaxaClient;
import com.september.fuelup.model.rest.DriverBalance;
import com.september.fuelup.model.rest.DriverInfo;
import com.september.fuelup.model.rest.RespStatus;
import com.september.fuelup.repository.DayPaymentLimitRepository;
import com.september.fuelup.repository.DriverToFuelUpRepository;
import com.september.fuelup.repository.DriversGroupRepository;
import com.september.fuelup.repository.ProviderRepository;
import com.september.fuelup.repository.TaxaClientRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DriverService {

	@Autowired private RestTemplateTaxi restTemplateTaxi;
	@Autowired private ProviderRepository providerRepository;
	@Autowired private DriverToFuelUpRepository driverToFuelUpRepository;
	@Autowired private DayPaymentLimitRepository dayPaymentLimitRepository;
	@Autowired private DriversGroupRepository driversGroupRepository;
	@Autowired private TaxaClientRepository taxaClientRepository;
	@PersistenceContext private EntityManager entityManager;
	
	public Driver getDriverById(Long id) {
		Session session = entityManager.unwrap(Session.class);
		Driver driver = session.createQuery(
			"FROM Driver WHERE id in " +
				"(SELECT driverId FROM DriverToFuelUp WHERE driverId=:driverId)",
				Driver.class
		)
		.setParameter("driverId", id)
		.getSingleResult();
		return driver;
	}
	
	private Optional<ServiceHasVariable> getServiceHasVariable(Long driverId, Integer variableId) {
		List<ServiceHasVariable> serviceHasVariable = entityManager.createQuery(
			"FROM ServiceHasVariable WHERE variableId=:variableId and " +
				" serviceId in (SELECT serviceId FROM Driver WHERE id=:id)",
			ServiceHasVariable.class
		)
		.setParameter("variableId", variableId)
		.setParameter("id", driverId)
		.setMaxResults(1)
		.getResultList();
		if(serviceHasVariable.isEmpty()) {
			return Optional.empty();
		}
		return Optional.ofNullable(serviceHasVariable.get(0));
	}

	public Optional<ServiceHasVariable> getServiceHasVariable172(Long driverId) {
		return getServiceHasVariable(driverId, 172);
	}
	
	public void add() {
		Session session = entityManager.unwrap(Session.class);
		for(Provider provider : providerRepository.findAll()) {
			if(provider.getProviderId() == null) {
				log.warn("prodiver for auth is null " + provider);
			}
			
			List<Driver> drivers = session.createQuery(
				"FROM Driver WHERE id in " +
					"(SELECT driverId FROM DriverToFuelUp WHERE changed=:changed " +
						"and providerId=:providerId)", Driver.class
			)
			.setParameter("changed", 1)
			.setParameter("providerId", provider.getId())
			.getResultList();
			
			for(Driver driver : drivers) {
				log.debug(driver);

				Map<String, String> map = new HashMap<>();
				map.put("id", String.valueOf(driver.getId()));
				map.put("phone", driver.getPhone());
				map.put("fio", driver.getFIO());

				restTemplateTaxi.setPartnerID(provider.getProviderId());
				try {
					RespStatus respStatus = restTemplateTaxi.postForObject("/driver/add", map, RespStatus.class);
					if(!respStatus.getStatus().equals(RespStatus.STATUS_OK)) {
						throw new RuntimeException("responce error " + respStatus);
					}
					log.info("/driver/add -> " + driver + ". " + respStatus);
					driverToFuelUpRepository.updateChangedByDriverId(0, driver.getId());
				}
				catch(Exception e) {
					log.error("error driverAdd " + driver, e);
				}
			}
		}
	}

	public void del() {
		Session session = entityManager.unwrap(Session.class);
		for(Provider provider : providerRepository.findAll()) {
			if(provider.getProviderId() == null) {
				log.warn("prodiver for auth is null " + provider);
			}
			
			List<Driver> drivers = session.createQuery(
				"FROM Driver WHERE id in " +
					"(SELECT driverId FROM DriverToFuelUp WHERE toDel=:toDel " +
						"and providerId=:providerId)", Driver.class
			)
			.setParameter("toDel", 1)
			.setParameter("providerId", provider.getId())
			.getResultList();

			for(Driver driver : drivers) {
				Map<String, String> map = new HashMap<>();
				map.put("id", String.valueOf(driver.getId()));
				
				restTemplateTaxi.setPartnerID(provider.getProviderId());
				RespStatus respStatus = restTemplateTaxi.postForObject("/driver/del", map, RespStatus.class);
				if(!respStatus.getStatus().equals(RespStatus.STATUS_OK)) {
					throw new RuntimeException("responce error " + respStatus);
				}
				log.info("/driver/del -> " + driver + ". " + respStatus);
				driverToFuelUpRepository.deleteByDriverId(driver.getId());
			}
		}
	}
	
	public DriverInfo getInfo(Long id) {
		Driver driver = getDriverById(id);
		if(driver == null) {
			throw new RuntimeException("driver not found");
		}

		DriverInfo driverInfo = new DriverInfo();
		driverInfo.setName(driver.getFIO());
		return driverInfo;
	}
	
	public DriverBalance getBalance(Long id) {
		Driver driver = getDriverById(id);
		if(driver == null) {
			throw new RuntimeException("driver not found");
		}
		DriverBalance balance = new DriverBalance();
		DriversGroup driversGroup = driversGroupRepository.findById(driver.getGroupId()).orElse(null);

		BigDecimal bal = driver.getBalance();
		BigDecimal min = new BigDecimal(getServiceHasVariable(driver.getId(), 171).get().getValue());
		
		BigDecimal balance1 = bal.subtract(min);
		log.debug("bal " + bal + " min " + min + " balance1 " + balance1);
		
		BigDecimal lim1 = getServiceHasVariable(driver.getId(), 172)
			.map(ServiceHasVariable::getValue)
			.map(BigDecimal::new)
			.orElse(null);
		if(driversGroup != null && driversGroup.getDayFuelLimit() != null) {
			log.debug("driversGroup.dayFuelLim != null -> " + driver);
			lim1 = driversGroup.getDayFuelLimit();
		}
		else if(lim1 == null) {
			balance.setBalance(balance1);
			log.debug("lim1 == null -> " + balance);
		}

		BigDecimal limit = BigDecimal.ZERO;
		DayPaymentLimit lim = dayPaymentLimitRepository.getFirstByDriverId(driver.getId()).orElse(null);
		if(lim == null || lim.getFuelLimit() == null) {
			limit = lim1;
			log.debug("lim == null -> " + driver + " -> " + limit);
		}
		else {
			limit = lim.getFuelLimit();
			log.debug("lim != null -> " + driver + " -> " + limit);
		}
		
		if(limit.equals(BigDecimal.ZERO)) {
			balance.setBalance(BigDecimal.ZERO);
			log.debug("limit.equals(BigDecimal.ZERO) -> " + driver + " -> " + balance);
		}
		else if(limit.compareTo(BigDecimal.ZERO) > 0 && balance1.compareTo(limit) >= 0) {
			balance.setBalance(limit.subtract(min));
			log.debug("limit.compareTo(BigDecimal.ZERO) > 0 && balance1.compareTo(limit) >= 0) -> " +
				driver + " -> limit " + limit + " balance1 " + balance1 + " min " + min + " " + balance);
		}
		else if(limit.compareTo(BigDecimal.ZERO) > 0 && balance1.compareTo(limit) < 0) {
			balance.setBalance(balance1);
			log.debug("limit.compareTo(BigDecimal.ZERO) > 0 && balance1.compareTo(limit) < 0) -> " +
				driver + " -> limit " + limit + " balance1 " + balance1 + " " + balance);
		}
		
		if(balance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
			balance.setBalance(BigDecimal.ZERO);
			log.debug("balance.getBalance().compareTo(BigDecimal.ZERO) < 0 -> " +
				driver + " -> " + balance);
		}
		
		BigDecimal serviceLimit = taxaClientRepository.findFirstByServiceId(driver.getServiceId())
			.map(TaxaClient::getBalance)
			.orElse(null);
		if(serviceLimit == null) {
			log.warn("serviceLimit == null -> " + driver);
		}
		else {
			if(balance.getBalance().compareTo(serviceLimit) >= 0) {
				log.debug("balance.getBalance().compareTo(serviceLimit) >= 0 -> " + driver);
				balance.setBalance(serviceLimit);
			}
		}
		
		balance.setBalance(balance.getBalance().setScale(2, RoundingMode.DOWN));
		return balance;
	}
}
