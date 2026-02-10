package com.urbanEats.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDto {
	private Long cartId;
	private Long userId;
	private List<CartItemDto> cartItems;
}
