package com.september.fuelup.model.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class RespResult implements Serializable {

	private static final long serialVersionUID = 8671606024041328927L;
	
	public static final String RESULT_DENY = "deny";
	public static final String RESULT_OK = "ok";
	
	private String result;
	
    @JsonInclude(value = Include.NON_NULL)
	private String message;
}
