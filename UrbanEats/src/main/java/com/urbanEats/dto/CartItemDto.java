package com.urbanEats.dto;

import lombok.Data;

@Data
public class CartItemDto {
	private Long cartItemId;
	private Long menuId;
	private String name;
	private String img;
	private int quantity;
	
	private Double originalPrice;
    private Double finalPrice;
    private Double discountAmount;
}
