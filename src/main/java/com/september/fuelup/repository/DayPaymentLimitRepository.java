package com.september.fuelup.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.september.fuelup.model.DayPaymentLimit;

public interface DayPaymentLimitRepository extends CrudRepository<DayPaymentLimit, Long> {

	Optional<DayPaymentLimit> getFirstByDriverId(Long driverId);
	
	@Modifying
	@Query("UPDATE DayPaymentLimit SET fuelLimit=:fuelLimit WHERE driverId=:driverId")
	int updateFuelLimitByDriverId(
		@Param("fuelLimit") BigDecimal fuelLimit,
		@Param("driverId") Long driverId
	);
	
	@Modifying
	@Query(value = "DELETE FROM dbo.DayPaymentLimit", nativeQuery = true)
	int allDelete();
}
