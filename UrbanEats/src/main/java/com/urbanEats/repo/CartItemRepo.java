package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long>{
	void deleteByCart(Cart cart);
}
