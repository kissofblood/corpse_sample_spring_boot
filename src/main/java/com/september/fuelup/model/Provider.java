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
@Table(name = "PROVIDERS", schema = "dbo")
public class Provider implements Serializable {

	private static final long serialVersionUID = 5386909615606874676L;

	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ProviderID")
	private String providerId;
	
	@Column(name = "[Type]")
	private Integer type;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "CompanyName")
	private String companyName;
	
	@Column(name = "Email")
	private String email;
	
	@Column(name = "Phone")
	private String phone;
	
	@Column(name = "INN")
	private String INN;
	
	@Column(name = "ContactName")
	private String contactName;
}
