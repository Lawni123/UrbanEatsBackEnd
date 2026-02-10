//package com.urbanEats.rest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//import com.urbanEats.entity.Order;
//import com.urbanEats.service.OrderService;
//
//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping("/place")
//    public ResponseEntity<Order> placeOrder(
//            @RequestBody PlaceOrderRequestDto request) {
//
//        return new ResponseEntity<>(
//                orderService.placeOrder(request),
//                HttpStatus.CREATED
//        );
//    }
//
//    @PutMapping("/cancel/{orderId}")
//    public ResponseEntity<Order> cancelOrder(
//            @PathVariable Long orderId) {
//
//        return ResponseEntity.ok(
//                orderService.cancelOrder(orderId)
//        );
//    }
//}
