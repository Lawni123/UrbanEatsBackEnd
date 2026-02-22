package com.urbanEats.entity;

import java.util.List;

import com.urbanEats.enums.OrderStatus;
import com.urbanEats.enums.OrderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Double totalAmount;

    private Double discountAmount;

    private Double finalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Payment payment;
}
