package com.september.fuelup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.september.fuelup.model.Provider;

public interface ProviderRepository extends CrudRepository<Provider, Long> {

	List<Provider> getByTypeAndProviderId(Integer type, String providerId);
	
	Optional<Provider> getFirstByProviderId(String providerId);
	
	@Modifying
	@Query("UPDATE Provider SET providerId=:providerId WHERE id=:id")
	int updateProviderIdById(@Param("providerId") String providerId, @Param("id") Long id);
}
