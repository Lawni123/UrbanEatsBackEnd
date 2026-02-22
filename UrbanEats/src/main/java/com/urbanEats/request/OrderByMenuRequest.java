package com.urbanEats.request;

import com.urbanEats.enums.OrderType;
import com.urbanEats.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderByMenuRequest {
	@NotNull
	private Long menuId;
	@NotNull
	private Integer quantity;
	@NotNull
	private  OrderType orderType;
}
