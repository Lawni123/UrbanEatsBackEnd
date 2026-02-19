package com.urbanEats.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OfferDto {
	private Long id;
	@NotBlank
	private String title;
	@NotBlank
	private String description;

	@NotNull
	private Double discountPercentage;
	@NotNull
	private Double flatDiscountAmount;

	@NotNull
	private LocalDate startDate;
	@NotNull
	private LocalDate endDate;

}
