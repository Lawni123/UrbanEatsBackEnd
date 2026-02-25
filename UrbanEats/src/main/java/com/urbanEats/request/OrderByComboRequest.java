package com.urbanEats.request;

import com.urbanEats.enums.OrderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderByComboRequest {
	@NotNull
	private Long comboId;
	@NotNull
	private Integer quantity;
	@NotNull
	private OrderType orderType;
	@NotBlank
	private String description;
}
