package com.urbanEats.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComboItemRequest {
	@NotNull
	private Long menuId;
	@NotNull
	private Integer quantity;
}
