package com.urbanEats.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long>{

}
