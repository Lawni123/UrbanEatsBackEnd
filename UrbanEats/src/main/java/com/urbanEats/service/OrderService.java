package com.urbanEats.service;


import com.urbanEats.entity.Order;
import com.urbanEats.request.OrderRequestDto;

public interface OrderService {

    Order placeOrder(OrderRequestDto orderDto);

    Order cancelOrder(Long orderId);
}
