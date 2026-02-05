package com.urbanEats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDto> userException(UserException ex){
		ErrorDto error = new ErrorDto();
		error.setErrorCode("USER_ERROR");
		error.setErrorMessage(ex.getMessage());
		error.setErrorStatus(ex.getHttpStatus());
		ex.printStackTrace();
		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> userException(Exception ex){
		ErrorDto error = new ErrorDto();
		error.setErrorCode("SERVER_ERROR");
		error.setErrorMessage(ex.getMessage());
		error.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
