package com.september.fuelup.model;

import java.io.Serializable;

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
@Table(name = "Service_has_Variable", schema = "dbo")
public class ServiceHasVariable implements Serializable {

	private static final long serialVersionUID = -5900924697674391351L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ServiceID")
	private Integer serviceId;
	
	@Column(name = "VariableID")
	private Integer variableId;
	
	@Column(name = "Value")
	private String value;
}
