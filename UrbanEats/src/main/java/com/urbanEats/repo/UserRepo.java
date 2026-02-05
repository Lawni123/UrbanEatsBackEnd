package com.urbanEats.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
}
