package com.urbanEats.dto;

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

    @NotBlank
    private String foodType;

    @NotBlank
    private String img;
}
