package com.urbanEats.dto;

import java.util.List;

import com.urbanEats.enums.OrderStatus;
import com.urbanEats.enums.OrderType;
import com.urbanEats.enums.PaymentMethod;
import com.urbanEats.enums.PaymentStatus;

import lombok.Data;

@Data
public class OrderDto {
	private Long orderId;
	private Long userId;
	private OrderType orderType;
	private OrderStatus orderStatus;
	private String description;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private List<OrderItemDto> orderItemDto;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus; 
}
