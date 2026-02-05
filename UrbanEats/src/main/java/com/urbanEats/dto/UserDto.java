package com.urbanEats.dto;

import com.urbanEats.enums.Role;

import lombok.Data;

@Data
public class UserDto {
	private Long userId;
	private String name;
	private String email;
	private String phone;
	private Role role;
	private Long cartId;
	
	//NO of items in the cart
	private String profileImgUrl;
	private String profileImgUrlPublicId;
}
