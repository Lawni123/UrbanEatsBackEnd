package com.urbanEats.service;

import java.util.Map;

import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;

public interface UserService {
	
	public String userSignup(SignupRequest request);
	public Map<String,Object> userLogin(LoginRequest request);
}
