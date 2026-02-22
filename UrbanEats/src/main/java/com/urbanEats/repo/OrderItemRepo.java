package com.urbanEats.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Order;
import com.urbanEats.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{
	
}
