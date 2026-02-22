package com.urbanEats.entity;

import com.urbanEats.enums.PaymentMethod;
import com.urbanEats.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;
    
    private String transactionId;
    private PaymentMethod paymentMethod;  // UPI, CARD, CASH
    private PaymentStatus paymentStatus;  // SUCCESS, FAILED, PENDING
}
