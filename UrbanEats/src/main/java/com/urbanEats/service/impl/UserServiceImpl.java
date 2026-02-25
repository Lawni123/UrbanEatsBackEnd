package com.urbanEats.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urbanEats.dto.UserDto;
import com.urbanEats.entity.User;
import com.urbanEats.exception.UserException;
import com.urbanEats.repo.UserRepo;
import com.urbanEats.request.LoginRequest;
import com.urbanEats.request.SignupRequest;
import com.urbanEats.service.CloudinaryService;
import com.urbanEats.service.EmailService;
import com.urbanEats.service.UserService;
import com.urbanEats.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CloudinaryService cloudinaryService;
	@Autowired
	private ModelMapper mapper;

	@Override
	public String userSignup(SignupRequest request) {

		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserException("User Email Already Exist", HttpStatus.CONFLICT);
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		User savedUser = userRepo.save(user);

		emailService.sendMail(request.getEmail(), "Welcome to UrbanEats",
				"Hello " + user.getName() + ", your BurgerAdda account has been created successfully.\r\n" + "\r\n"
						+ "Start ordering your favorite burgers and enjoy delicious meals with us!\r\n" + "\r\n"
						+ "Regards,\r\n" + "UrbanEats Team");

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", request.getRole().name());
		claims.put("userId", savedUser.getUserId());
		claims.put("email", request.getEmail());

		return jwtUtil.generateToken(request.getEmail(), claims);

	}

	@Override
	public Map<String, Object> userLogin(LoginRequest request) {

		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserException("User Does Not Exist", HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UserException("Password Mismatch", HttpStatus.UNAUTHORIZED);
		}

		UserDto userDto = mapper.map(user, UserDto.class);
		if (user.getCart() != null)
			userDto.setCartId(user.getCart().getCartId());

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole().name());
		claims.put("userId", user.getUserId());
		claims.put("email", request.getEmail());

		String token = jwtUtil.generateToken(request.getEmail(), claims);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("token", token);
		map.put("userDto", userDto);

		return map;
	}

	@Override
	public UserDto update(Long userId, MultipartFile imageFile, String isChanged, String name) {

		User findUser = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found", HttpStatus.NOT_FOUND));

		if (imageFile != null && imageFile.getSize() > 2 * 1024 * 1024) {
			throw new UserException("Image size must be less than 2MB", HttpStatus.BAD_REQUEST);
		}

		if (isChanged.equalsIgnoreCase("true")) {
			if (findUser.getProfileImgUrlPublicId() != null) {
				cloudinaryService.deleteFile(findUser.getProfileImgUrlPublicId());
				findUser.setProfileImgUrl(null);
				findUser.setProfileImgUrlPublicId(null);
			}

			if (imageFile != null) {
				Map uploadResult = cloudinaryService.uploadFile(imageFile);
				findUser.setProfileImgUrl((String) uploadResult.get("secure_url"));
				findUser.setProfileImgUrlPublicId((String) uploadResult.get("public_id"));

			}

		}
		findUser.setName(name);
		return mapper.map(userRepo.save(findUser), UserDto.class);
	}

	@Override
	public UserDto addKitchenStaff(SignupRequest request) {

		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserException("User Email Already Exist", HttpStatus.CONFLICT);
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		User savedUser = userRepo.save(user);

		emailService.sendMail(
		        request.getEmail(),
		        "Welcome to UrbanEats - Kitchen Staff Account Created",
		        "Hello " + user.getName() + ",\r\n\r\n"
		                + "Your Kitchen Staff account has been created successfully by the Admin.\r\n"
		                + "You can now log in to the UrbanEats system and start managing kitchen operations.\r\n\r\n"
		                + "If you have any questions, please contact the Admin.\r\n\r\n"
		                + "Regards,\r\n"
		                + "UrbanEats Team"
		);


		return mapper.map(userRepo.save(savedUser), UserDto.class);
	}

}
