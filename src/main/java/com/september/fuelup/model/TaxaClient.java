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
@Table(name = "taxaClient", schema = "dbo")
public class TaxaClient implements Serializable {

	private static final long serialVersionUID = -2822635772414498028L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "serviceId")
	private Long serviceId;
	
	@Column(name = "balance")
	private BigDecimal balance;
}
