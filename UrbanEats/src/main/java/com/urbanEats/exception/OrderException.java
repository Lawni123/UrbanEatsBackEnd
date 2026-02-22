package com.urbanEats.exception;

import org.springframework.http.HttpStatus;

public class OrderException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	
	public OrderException(String message,HttpStatus httpStatus) {
		super(message);
		this.httpStatus=httpStatus;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
