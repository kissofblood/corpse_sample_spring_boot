package com.september.fuelup.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @ToString
@Table(name = "DriversToFuelUp", schema = "dbo")
public class DriverToFuelUp implements Serializable {

	private static final long serialVersionUID = -8501168106627062192L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "DriverID")
	private Long driverId;
	
	@Column(name = "changed")
	private Integer changed;
	
	@Column(name = "ProviderID")
	private Long providerId;
	
	@Column(name = "holdMoney")
	private BigDecimal holdMoney;
	
	@Column(name = "orders")
	private String orders;
	
	@Column(name = "ServiceID")
	private Long serviceID;
	
	@Column(name = "toDel")
	private Integer toDel;
}
