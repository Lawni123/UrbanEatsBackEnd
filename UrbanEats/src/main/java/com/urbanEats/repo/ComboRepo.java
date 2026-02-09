package com.urbanEats.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Combo;

public interface ComboRepo extends JpaRepository<Combo, Long>{

	List<Combo> findByComboNameContainingIgnoreCase(String input);

}
