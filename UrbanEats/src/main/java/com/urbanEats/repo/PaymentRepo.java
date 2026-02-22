package com.urbanEats.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Cart;
import com.urbanEats.entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long>{
	Optional<Payment> findByTransactionId(String txnId);
}
