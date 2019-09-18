package com.september.fuelup.model.rest;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class RespStatus implements Serializable {

	private static final long serialVersionUID = 7088493386238818882L;
	
	public static final String STATUS_OK = "OK";
	
	private String status;
}
