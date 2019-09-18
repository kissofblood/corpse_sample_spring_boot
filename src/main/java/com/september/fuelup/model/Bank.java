package com.september.fuelup.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @ToString
@Table(name = "BANK", schema = "dbo")
public class Bank implements Serializable {

	private static final long serialVersionUID = -3123203692172889716L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "DriverID")
	private Long driverId;
	
	@Column(name = "Sum")
	private BigDecimal sum;
	
	@Column(name = "Descr")
	private String description;
	
	@Column(name = "dtArrive")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dtArrive;

	@Column(name = "ServiceID")
	private Long serviceID;
	
	@Column(name = "trStatus")
	private Integer trStatus;
	
	@Column(name = "trType")
	private Integer trType;
	
	@Column(name = "FuelCardTransactionID")
	private Integer fuelCardTransactionID;
}
