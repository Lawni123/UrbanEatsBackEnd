package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.ComboItem;

public interface ComboItemRepo extends JpaRepository<ComboItem, Long>{

}
