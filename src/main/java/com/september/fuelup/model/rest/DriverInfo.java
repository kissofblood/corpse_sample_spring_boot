package com.september.fuelup.model.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class DriverInfo implements Serializable {

	private static final long serialVersionUID = -5765624587465232983L;

	private String name;

    @JsonInclude(value = Include.NON_NULL)
	private String additional;
}
