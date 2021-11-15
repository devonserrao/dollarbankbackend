package com.cognixia.jump.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OverdraftException extends Exception {

	private static final long serialVersionUID = 1L;

	public OverdraftException(String message) {
		super(message);
	}
	
}
