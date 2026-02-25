package com.urbanEats.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.urbanEats.dto.UserDto;
import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;

public interface UserService {
	
	String userSignup(SignupRequest request);
	Map<String,Object> userLogin(LoginRequest request);
	UserDto update(Long userId,MultipartFile imageFile,String isChanged,String name);
	UserDto addKitchenStaff(SignupRequest request);
}
