package com.urbanEats.service;

import java.util.List;

import com.urbanEats.request.ComboRequest;

public interface ComboService {
	void addCombo(ComboRequest request);
	ComboRequest getCombo(Integer id);
	List<ComboRequest> getCombos(String input);
	List<ComboRequest> getCombos();
	ComboRequest updateCombo();
	void deleteCombo(Integer id);
}
