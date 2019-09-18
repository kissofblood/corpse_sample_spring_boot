package com.september.fuelup.repository;

import org.springframework.data.repository.CrudRepository;

import com.september.fuelup.model.Bank;

public interface BankRepository extends CrudRepository<Bank, Long> {
}
