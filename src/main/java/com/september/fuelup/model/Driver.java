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
@Table(name = "DRIVERS", schema = "dbo")
public class Driver implements Serializable {

	private static final long serialVersionUID = -7293933901676198144L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "Phones")
	private String phone;
	
	@Column(name = "FIO")
	private String FIO;
	
	@Column(name = "ServiceID")
	private Long serviceId;
	
	@Column(name = "Balance")
	private BigDecimal balance;
	
	@Column(name = "groupId")
	private Long groupId;
}
