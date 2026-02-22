package com.urbanEats.entity;

import java.util.List;

import com.urbanEats.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String name;
	@Column(unique = true)
	private String email;
	private String phone;
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role; // CUSTOMER, ADMIN, KITCHEN

	// 1 User -> 1 Cart
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Cart cart;

	// 1 User -> Many Orders
	@OneToMany(mappedBy = "user")
	private List<Order> orders;
			
	private String profileImgUrl;

	private String profileImgUrlPublicId;
}
