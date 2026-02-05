package com.urbanEats.service;

import java.util.List;

import com.urbanEats.dto.MenuDto;

public interface MenuService {
	MenuDto addItem(MenuDto menuRequest);
	MenuDto getItem(Integer id);
	List<MenuDto> getItems();
	List<MenuDto> getItems(String input);
	MenuDto updateItem(MenuDto menuDto);
	Boolean deleteItem(Integer id);
}
