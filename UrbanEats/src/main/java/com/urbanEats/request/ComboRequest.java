package com.urbanEats.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComboRequest {
	@NotBlank
	private String comboName;
	@NotBlank
	private String img;
	@NotNull
	private List<ComboItemRequest> comboItems;
}
