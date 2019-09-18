package com.september.fuelup.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.september.fuelup.model.TaxaClient;

public interface TaxaClientRepository extends CrudRepository<TaxaClient, Long> {

	Optional<TaxaClient> findFirstByServiceId(Long serviceId);
}
