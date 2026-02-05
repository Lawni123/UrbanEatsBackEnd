package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Long>{

}
