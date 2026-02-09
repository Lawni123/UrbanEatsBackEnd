package com.urbanEats.dto;

import lombok.Data;

@Data
public class ComboItemDto {
	private Long comboItemId;
	private Long menuId;
	private String itemName;
	private int quantity;
}
