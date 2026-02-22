package com.urbanEats.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanEats.dto.OrderDto;
import com.urbanEats.enums.OrderStatus;
import com.urbanEats.enums.OrderType;
import com.urbanEats.enums.PaymentMethod;
import com.urbanEats.exception.OrderException;
import com.urbanEats.request.OrderByComboRequest;
import com.urbanEats.request.OrderByMenuRequest;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.CustomUserDetails;
import com.urbanEats.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders/secure")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/menu")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> createOrderFromMenu(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody OrderByMenuRequest request,
			BindingResult bindingResult
			){
		System.out.println(request);
		if(bindingResult.hasErrors()) {
			throw new OrderException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		OrderDto orderDto = orderService.createOrderFromMenu(customUserDetails.getUser().getUserId(), request);
		
		ApiResponse<OrderDto> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	


	@PostMapping("/combo")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> createOrderFromCombo(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody OrderByComboRequest request,
			BindingResult bindingResult
			){
		if(bindingResult.hasErrors()) {
			throw new OrderException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		OrderDto orderDto = orderService.createOrderFromCombo(customUserDetails.getUser().getUserId(), request);
		
		ApiResponse<OrderDto> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/cart")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> createOrderFromCart(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam OrderType orderType
			){
		
		OrderDto orderDto = orderService.createOrderFromCart(customUserDetails.getUser().getUserId(),orderType );
		
		ApiResponse<OrderDto> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/get/{orderId}")
	public ResponseEntity<?> getOrderById(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long orderId
			){
		
		OrderDto orderDto = orderService.getOrderById(orderId);
		
		ApiResponse<OrderDto> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDto);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getOrdersByUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails
			){
		
		List<OrderDto> orderDtoList = orderService.getOrdersByUser(customUserDetails.getUser().getUserId());
		
		ApiResponse<List<OrderDto>> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDtoList);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getOrdersByUserId(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long userId
			){
		
		List<OrderDto> orderDtoList = orderService.getOrdersByUser(userId);
		
		ApiResponse<List<OrderDto>> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDtoList);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllOrders(
			@AuthenticationPrincipal CustomUserDetails customUserDetails
			){
		
		List<OrderDto> orderDtoList = orderService.getAllOrders();
		
		ApiResponse<List<OrderDto>> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDtoList);
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/cancel/{orderId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> cancelOrder(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long orderId
			){
		
		orderService.cancelOrder(customUserDetails.getUser().getUserId(), orderId);
		
		return ResponseEntity.ok("SuccessFully Cancelled the Order");
	}
	
	@PutMapping("/update/{orderId}")
	@PreAuthorize("hasRole('KITCHEN')")
	public ResponseEntity<?> updateOrderStatus(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long orderId,
			@RequestParam OrderStatus status
			){
		OrderDto orderDto = orderService.updateOrderStatus(orderId,status);
		
		ApiResponse<OrderDto> response = new ApiResponse<>();
		response.setMessage("Succesfully Orderd");
		response.setStatus("success");
		response.setData(orderDto);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("{orderId}/payment")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> initiatePayment(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long orderId,
			@RequestParam PaymentMethod paymentMethod
			){
		String txnId = orderService.initiatePayment(customUserDetails.getUser().getUserId(),orderId, paymentMethod);
		
		Map<String,String> response = new HashMap<>();
		response.put("status", "success");
		response.put("transactionId", txnId);
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/payment/{transactionId}/confirm")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> confirmPayment(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable String transactionId,
			@RequestParam Boolean success
			){
		String message = orderService.confirmPayment(customUserDetails.getUser().getUserId(), transactionId, success);
		
		ApiResponse<String> response = new ApiResponse<>();
		response.setMessage(message);
		response.setStatus("success");
		
		return ResponseEntity.ok(response);
		
	}
}
