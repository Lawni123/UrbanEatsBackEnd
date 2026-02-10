//package com.urbanEats.service.impl;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.urbanEats.entity.Cart;
//import com.urbanEats.entity.Order;
//import com.urbanEats.entity.OrderItem;
//import com.urbanEats.entity.User;
//import com.urbanEats.repo.CartRepo;
//import com.urbanEats.repo.OrderRepo;
//import com.urbanEats.repo.UserRepo;
//import com.urbanEats.request.OrderRequestDto;
//import com.urbanEats.service.OrderService;
//
//import jakarta.transaction.Transactional;
//
//@Service
//@Transactional
//public class OrderServiceImpl implements OrderService {
//
//    @Autowired
//    private OrderRepo orderRepo;
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private CartRepo cartRepo;
//
//    @Override
//    public Order placeOrder(OrderRequestDto orderDto) {
//
//        User user = userRepo.findById(orderDto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = cartRepo.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Cart is empty"));
//
//        Order order = new Order();
//        order.setUser(user);
//        order.setOrderType(orderDto.getOrderType());
//        order.setTableNumber(orderDto.getTableNumber());
//
//        List<OrderItem> orderItems = cart.getCartItems()
//                .stream()
//                .map(cartItem -> {
//                    OrderItem item = new OrderItem();
//                    item.setOrder(order);
//                    item.setFood(cartItem.getFood());
//                    item.setQuantity(cartItem.getQuantity());
//                    item.setPrice(cartItem.getPrice());
//                    return item;
//                })
//                .collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//
//        double totalAmount = orderItems.stream()
//                .mapToDouble(i -> i.getPrice() * i.getQuantity())
//                .sum();
//
//        double discount = totalAmount > 500 ? 50 : 0;
//
//        order.setTotalAmount(totalAmount);
//        order.setDiscountAmount(discount);
//        order.setFinalAmount(totalAmount - discount);
//
//        Order savedOrder = orderRepo.save(order);
//
//        // clear cart after placing order
//        cartRepo.delete(cart);
//
//        return savedOrder;
//    }
//
//    @Override
//    public Order cancelOrder(Long orderId) {
//
//        Order order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // If orderStatus field exists, uncomment below
//        // order.setOrderStatus(OrderStatus.CANCELLED);
//
//        return orderRepo.save(order);
//    }
//}
