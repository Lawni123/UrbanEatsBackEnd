package com.urbanEats.dto;

import com.urbanEats.enums.FoodType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuDto {


    private Long id;   

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private FoodType foodType;

    @NotBlank
    private String img;
    
    private Double originalPrice;
    private Double finalPrice;
    private Double discountAmount;
    private String offerTitle;
    private Boolean offerApplied;


}
