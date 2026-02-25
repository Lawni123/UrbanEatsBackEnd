package com.urbanEats.request;

import com.urbanEats.enums.OrderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderByCartRequest {
	@NotNull
	OrderType orderType;
	@NotBlank
	private String description;
}
