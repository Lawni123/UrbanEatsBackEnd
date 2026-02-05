package com.urbanEats.response;

import lombok.Data;

@Data //T for generic type it will except all types of classes
public class ApiResponse<T> {
	private String status;
	
	private String message;
	
	private T data;
	
}
