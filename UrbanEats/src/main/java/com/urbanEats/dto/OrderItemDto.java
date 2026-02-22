package com.urbanEats.dto;

import lombok.Data;

@Data
public class OrderItemDto {
	private Long orderItemId;
	private Long menuId;
	private Integer quantity;
	private Double price;
	private String name;
}
