package com.urbanEats.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.MenuDto;
import com.urbanEats.entity.Menu;
import com.urbanEats.exception.MenuException;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private ModelMapper modelMapper;

    // -------- ADD ITEM --------
    @Override
    public MenuDto addItem(MenuDto menuRequest) {

        Menu menu = modelMapper.map(menuRequest, Menu.class);

        Menu saved = menuRepo.save(menu);

        return modelMapper.map(saved, MenuDto.class);
    }

    // -------- GET ITEM BY ID --------
    @Override
    public MenuDto getItem(Integer id) {

        Menu menu = menuRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new MenuException("Menu Item Not Found",HttpStatus.NOT_FOUND));

        return modelMapper.map(menu, MenuDto.class);
    }

    // -------- GET ALL ITEMS --------
    @Override
    public List<MenuDto> getItems() {

        return menuRepo.findAll()
                .stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .collect(Collectors.toList());
    }

    // -------- SEARCH ITEMS --------
    @Override
    public List<MenuDto> getItems(String input) {

        return menuRepo.findByNameContainingIgnoreCase(input)
                .stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .collect(Collectors.toList());
    }

    // -------- UPDATE ITEM (using name) --------
    @Override
    public MenuDto updateItem(MenuDto menuDto) {

        Menu existing = menuRepo.findById(menuDto.getId())
        		.orElseThrow(()->new MenuException("Menu Item Not found",HttpStatus.NOT_FOUND));

        modelMapper.map(menuDto, existing);

        Menu updated = menuRepo.save(existing);

        return modelMapper.map(updated, MenuDto.class);
    }

    // -------- DELETE ITEM --------
    @Override
    public void deleteItem(Integer id) {

        Menu menu = menuRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new MenuException("Menu Item Not Found",HttpStatus.NOT_FOUND));

        menuRepo.delete(menu);
    }
}