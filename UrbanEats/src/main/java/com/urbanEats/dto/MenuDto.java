package com.urbanEats.dto;

import com.urbanEats.enums.FoodType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuDto {
<<<<<<< HEAD

    private Long id;   

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotBlank
    private String foodType;

    @NotBlank
    private String img;
=======
	private Long menuId;
	@NotBlank
	private String name;
	@NotNull
	private Double price;
	@NotNull
	private FoodType foodType;
	@NotBlank
	private String img;
>>>>>>> branch 'master' of https://github.com/Lawni123/UrbanEatsBackEnd.git
}
