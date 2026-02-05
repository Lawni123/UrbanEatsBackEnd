package com.urbanEats.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanEats.exception.UserException;
import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;
import com.urbanEats.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/auth/signup")
	public ResponseEntity<?> signUp(
			@Valid @RequestBody SignupRequest signupRequest,
			BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			throw new UserException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		
		String token=userService.userSignup(signupRequest);
		
		Map<String,String> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("token", token);
		response.put("message", "User Signup Successful");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);	
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(
			@Valid @RequestBody LoginRequest request,
			BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			throw new UserException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		Map<String,Object> response = userService.userLogin(request);
		
		response.put("status", "success");
		response.put("message", "Login Successful");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
