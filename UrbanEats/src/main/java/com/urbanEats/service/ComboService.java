package com.urbanEats.service;

import java.util.List;

import com.urbanEats.dto.ComboDto;
import com.urbanEats.request.ComboRequest;

public interface ComboService {
	ComboDto addCombo(ComboRequest request);
	ComboDto getCombo(Long id);
	List<ComboDto> getCombos(String input);
	List<ComboDto> getCombos();
	ComboDto updateCombo(ComboRequest request);
	void deleteCombo(Long id);
}
