package com.september.fuelup.confuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.september.fuelup.model.rest.RespStatus;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalRestExceptionHandler {

	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public RespStatus handler(Exception e) {
		RespStatus resp = new RespStatus();
		resp.setStatus(e == null ? "null" : e.getMessage());
		log.error(e);
		return resp;
	}
}
