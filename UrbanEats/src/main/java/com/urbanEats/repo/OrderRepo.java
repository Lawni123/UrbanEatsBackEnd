package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{

}
