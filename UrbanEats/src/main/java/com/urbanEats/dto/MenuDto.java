package com.urbanEats.dto;

import com.urbanEats.enums.FoodType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class MenuDto {
	private Long menuId;
	@NotBlank
	private String name;
	@NotNull
	private Double price;
	@NotNull
	private FoodType foodType;
	@NotBlank
	private String img;
}
