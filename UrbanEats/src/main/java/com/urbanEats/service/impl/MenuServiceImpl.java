package com.urbanEats.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.MenuDto;
import com.urbanEats.entity.Menu;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepo menuRepo;

    // -------- ADD ITEM --------
    @Override
    public MenuDto addItem(MenuDto menuRequest) {

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuRequest, menu);

        Menu saved = menuRepo.save(menu);

        MenuDto dto = new MenuDto();
        BeanUtils.copyProperties(saved, dto);

        return dto;
    }

    // -------- GET ITEM BY ID --------
    @Override
    public MenuDto getItem(Integer id) {

        Menu menu = menuRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Menu Item Not Found"));

        MenuDto dto = new MenuDto();
        BeanUtils.copyProperties(menu, dto);

        return dto;
    }

    // -------- GET ALL ITEMS --------
    @Override
    public List<MenuDto> getItems() {

        return menuRepo.findAll()
                .stream()
                .map(menu -> {
                    MenuDto dto = new MenuDto();
                    BeanUtils.copyProperties(menu, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // -------- SEARCH ITEMS BY NAME --------
    @Override
    public List<MenuDto> getItems(String input) {

        return menuRepo.findByNameContainingIgnoreCase(input)
                .stream()
                .map(menu -> {
                    MenuDto dto = new MenuDto();
                    BeanUtils.copyProperties(menu, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // -------- UPDATE ITEM (using name since DTO has no id) --------
    @Override
    public MenuDto updateItem(MenuDto menuDto) {

        Menu existing = menuRepo
                .findByNameContainingIgnoreCase(menuDto.getName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Menu Item Not Found"));

        BeanUtils.copyProperties(menuDto, existing);

        Menu updated = menuRepo.save(existing);

        MenuDto dto = new MenuDto();
        BeanUtils.copyProperties(updated, dto);

        return dto;
    }

    // -------- DELETE ITEM --------
    @Override
    public void deleteItem(Integer id) {

        Menu menu = menuRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Menu Item Not Found"));

        menuRepo.delete(menu);
    }
}
