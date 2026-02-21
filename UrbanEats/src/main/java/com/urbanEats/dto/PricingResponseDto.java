package com.urbanEats.dto;


import lombok.Data;

@Data
public class PricingResponseDto {
    private Double originalPrice;
    private Double finalPrice;
    private Double discountAmount;
    private String offerTitle;
    private Boolean offerApplied;
}
