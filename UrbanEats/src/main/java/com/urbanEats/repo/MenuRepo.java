package com.urbanEats.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Menu;

public interface MenuRepo extends JpaRepository<Menu, Long>{
	List<Menu> findByNameContainingIgnoreCase(String name);
	


}
