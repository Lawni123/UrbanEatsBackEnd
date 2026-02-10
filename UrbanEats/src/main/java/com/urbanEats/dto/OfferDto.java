package com.urbanEats.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OfferDto {
	private String title;
	private String description;

	private Double discountPercentage;
	private Double flatDiscountAmount;

	private LocalDate startDate;
	private LocalDate endDate;

}
