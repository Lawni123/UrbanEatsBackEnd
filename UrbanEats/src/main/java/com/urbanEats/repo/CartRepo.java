package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Long>{

}
