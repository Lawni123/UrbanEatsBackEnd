package com.urbanEats.service;

import com.urbanEats.dto.CartDto;

public interface CartService {
	CartDto addItemToCart(Long userId,Long menuId);
	CartDto removeItemFromCart(Long userId,Long cartItemId);
	CartDto getCartByUserId(Long userId);
}
