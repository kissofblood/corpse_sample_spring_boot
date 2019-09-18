package com.september.fuelup.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "DayPaymentLimit", schema = "dbo")
public class DayPaymentLimit implements Serializable {

	private static final long serialVersionUID = 5979270239250005436L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "[Date]")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name = "DriverID")
	private Long driverId;
	
	@Column(name = "FuelLimit")
	private BigDecimal fuelLimit;
	
	@Column(name = "PaymentLimit")
	private BigDecimal paymentLimit;
}
