package com.september.fuelup.model.rest;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.ToString;

import lombok.Getter;

import lombok.Setter;

@Setter @Getter @ToString
public class DriverBalance implements Serializable {

	private static final long serialVersionUID = -4027851109397330077L;

	private BigDecimal balance;
}
