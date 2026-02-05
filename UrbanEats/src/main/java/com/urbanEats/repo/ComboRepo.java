package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Combo;

public interface ComboRepo extends JpaRepository<Combo, Long>{

}
