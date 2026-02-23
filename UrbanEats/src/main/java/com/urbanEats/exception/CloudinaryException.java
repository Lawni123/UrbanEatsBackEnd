package com.urbanEats.exception;

import org.springframework.http.HttpStatus;

public class CloudinaryException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	
	public CloudinaryException(String message,HttpStatus httpStatus) {
		super(message);
		this.httpStatus=httpStatus;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
