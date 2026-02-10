package com.urbanEats.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanEats.dto.CartDto;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.CartService;
import com.urbanEats.service.CustomUserDetails;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/secure/add/{menuId}")
	public ResponseEntity<?> addToCart(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long menuId){
		
		CartDto cartDto = cartService.addItemToCart(customUserDetails.getUser().getUserId(), menuId);
		
		ApiResponse<CartDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Item Added To Cart");
		response.setData(cartDto);
		
		return ResponseEntity.ok(response);
 	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/secure/remove/{cartItemId}")
	public ResponseEntity<?> removeFromCart(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long cartItemId){
		CartDto cartDto = cartService.removeItemFromCart(customUserDetails.getUser().getUserId(), cartItemId);
		
		ApiResponse<CartDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Item Removed From Cart");
		response.setData(cartDto);
		
		return ResponseEntity.ok(response);
 	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/secure/get")
	public ResponseEntity<?> getCartByUserId(
			@AuthenticationPrincipal CustomUserDetails customUserDetails){
		CartDto cartDto = cartService.getCartByUserId(customUserDetails.getUser().getUserId());
		
		ApiResponse<CartDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched");
		response.setData(cartDto);
		
		return ResponseEntity.ok(response);
 	}
}
