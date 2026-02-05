package com.urbanEats.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorDto {
	private String errorCode;
	private String errorMessage;
	private HttpStatus errorStatus;
}
