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
@Table(name = "DRIVERES_GROUP", schema = "dbo")
public class DriversGroup implements Serializable {

	private static final long serialVersionUID = -3665847819415971698L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "dayFuelLim")
	private BigDecimal dayFuelLimit;
	
}
