package com.urbanEats.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.UserDto;
import com.urbanEats.entity.User;
import com.urbanEats.exception.UserException;
import com.urbanEats.repo.UserRepo;
import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.EmailService;
import com.urbanEats.service.UserService;
import com.urbanEats.util.JwtUtil;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ModelMapper mapper;
	@Override
	public String userSignup(SignupRequest request) {
		
		if(userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserException("User Email Already Exist", HttpStatus.CONFLICT);
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
		
		User savedUser = userRepo.save(user);
		
		emailService.sendMail(request.getEmail(),
				"Welcome to UrbanEats",
				 "Hello "+user.getName()+", your BurgerAdda account has been created successfully.\r\n"
				 + "\r\n"
				 + "Start ordering your favorite burgers and enjoy delicious meals with us!\r\n"
				 + "\r\n"
				 + "Regards,\r\n"
				 + "UrbanEats Team");
		
		Map<String,Object> claims = new HashMap<>();
		claims.put("role", request.getRole().name());
		claims.put("userId", savedUser.getUserId());
		claims.put("email", request.getEmail());
		
		return jwtUtil.generateToken(request.getEmail(), claims);
		
	}

	@Override
	public Map<String, Object> userLogin(LoginRequest request) {
		
		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(()->new UserException("User Does Not Exist", HttpStatus.NOT_FOUND));
		
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UserException("Password Mismatch", HttpStatus.UNAUTHORIZED);
		}
		
		UserDto userDto = mapper.map(user, UserDto.class);
		if(user.getCart()!=null)
			userDto.setCartId(user.getCart().getCartId());
		
		Map<String,Object> claims = new HashMap<>();
		claims.put("role", user.getRole().name());
		claims.put("userId", user.getUserId());
		claims.put("email", request.getEmail());
		
		String token=  jwtUtil.generateToken(request.getEmail(), claims);
		
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("token", token);
		map.put("userDto", userDto);
	
		return map;
	}

}
