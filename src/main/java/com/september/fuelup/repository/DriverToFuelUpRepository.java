package com.september.fuelup.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.september.fuelup.model.DriverToFuelUp;

public interface DriverToFuelUpRepository extends CrudRepository<DriverToFuelUp, Long> {
	
	@Modifying
	@Query("UPDATE DriverToFuelUp SET holdMoney=:holdMoney, orders=:orders WHERE driverId=:driverId")
	int updateHoldMoneyAndOrdersByDriverId(
		@Param("holdMoney") BigDecimal holdMoney,
		@Param("orders") String orders,
		@Param("driverId") Long driverId
	);
	
	Optional<DriverToFuelUp> getFirstByDriverId(Long driverId);
	
	@Modifying
	@Query("DELETE FROM DriverToFuelUp WHERE driverId=:driverId")
	int deleteByDriverId(@Param("driverId") Long driverId);
	
	@Modifying
	@Query("UPDATE DriverToFuelUp SET changed=:changed WHERE driverId=:driverId")
	int updateChangedByDriverId(@Param("changed") Integer changed, @Param("driverId") Long driverId);
}
