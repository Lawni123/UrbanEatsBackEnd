package com.urbanEats.dto;

import java.util.List;

import lombok.Data;

@Data
public class ComboDto {
	private Long comboId;
	private String comboName;
	private Double comboPrice;
	private String Image;
	private List<ComboItemDto> items;
	
	 private Double originalPrice;
	    private Double finalPrice;
	    private Double discountAmount;
	    private String offerTitle;
	    private Boolean offerApplied;
}
