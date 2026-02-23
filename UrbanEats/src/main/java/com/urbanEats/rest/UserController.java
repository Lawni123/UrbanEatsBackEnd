package com.urbanEats.rest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanEats.dto.UserDto;
import com.urbanEats.entity.User;
import com.urbanEats.exception.UserException;
import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.CustomUserDetails;
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
	
	@GetMapping("/secure/profile")
	ResponseEntity<?> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("user", user);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping(value = "/secure/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
	public ResponseEntity<ApiResponse<?>> updateUserProfile(
	        @AuthenticationPrincipal CustomUserDetails customUserDetails,
	        @RequestParam(name = "img" ,required = false) MultipartFile imageFile,
	        @RequestParam String name,
	        @RequestParam String isChanged,
	        @RequestParam(required = true) Long userId
	) {
		
	    if (customUserDetails.getUser().getUserId() != userId.longValue()) {
	        throw new UserException("Unauthorized", HttpStatus.UNAUTHORIZED);
	    }
		
	    UserDto updated = userService.update(userId,imageFile,isChanged,name);

	    ApiResponse<UserDto> response = new ApiResponse<>();
	    response.setMessage("Profile Updated Successfully");
	    response.setStatus("success");
	    response.setData(updated);
	    return ResponseEntity.ok(response);
	}

}
