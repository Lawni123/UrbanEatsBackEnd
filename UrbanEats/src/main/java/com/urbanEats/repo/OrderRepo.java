package com.urbanEats.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{
	List<Order> findByUserUserId(Long userId);
}
