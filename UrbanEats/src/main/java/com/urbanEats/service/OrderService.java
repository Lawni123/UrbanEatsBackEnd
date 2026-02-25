package com.urbanEats.service;

import java.util.List;

import com.urbanEats.dto.OrderDto;
import com.urbanEats.enums.OrderStatus;
import com.urbanEats.enums.OrderType;
import com.urbanEats.enums.PaymentMethod;
import com.urbanEats.request.OrderByCartRequest;
import com.urbanEats.request.OrderByComboRequest;
import com.urbanEats.request.OrderByMenuRequest;



public interface OrderService {


    OrderDto createOrderFromMenu(Long userId, OrderByMenuRequest request);

    OrderDto createOrderFromCombo(Long userId, OrderByComboRequest request);

    OrderDto createOrderFromCart(Long userId,OrderByCartRequest request);


    OrderDto getOrderById(Long orderId);

    List<OrderDto> getOrdersByUser(Long userId);
    List<OrderDto> getAllOrders();

    void cancelOrder(Long userId,Long orderId);


    OrderDto updateOrderStatus(Long orderId, OrderStatus status);



    String initiatePayment(Long userId,Long orderId,PaymentMethod paymentMethod);

    String confirmPayment(Long userId,String transactionId, boolean success);

}