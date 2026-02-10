package com.urbanEats.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.urbanEats.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Long>{
	
	@Query("select c from Cart c where c.user.userId = :id")
	Optional<Cart> findByUserId(@Param("id") Long id);
	
}
