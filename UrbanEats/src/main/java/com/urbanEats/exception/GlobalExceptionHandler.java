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
	
	@ExceptionHandler(ComboException.class)
	public ResponseEntity<ErrorDto> comboException(ComboException ex){
		ErrorDto error = new ErrorDto();
		error.setErrorCode("COMBO_ERROR");
		error.setErrorMessage(ex.getMessage());
		error.setErrorStatus(ex.getHttpStatus());
		ex.printStackTrace();
		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}
	
	@ExceptionHandler(CartException.class)
	public ResponseEntity<ErrorDto> cartException(CartException ex){
		ErrorDto error = new ErrorDto();
		error.setErrorCode("CART_ERROR");
		error.setErrorMessage(ex.getMessage());
		error.setErrorStatus(ex.getHttpStatus());
		ex.printStackTrace();
		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}
	
	@ExceptionHandler(MenuException.class)
	public ResponseEntity<ErrorDto> menuException(MenuException ex){
		ErrorDto error = new ErrorDto();
		error.setErrorCode("MENU_ERROR");
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
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
