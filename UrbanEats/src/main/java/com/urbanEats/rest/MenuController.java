package com.urbanEats.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.urbanEats.dto.MenuDto;
import com.urbanEats.service.MenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // Only ADMIN can add menu item
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public MenuDto addItem(@Valid @RequestBody MenuDto dto) {
        return menuService.addItem(dto);
    }

    //  ADMIN and USER can view single item
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public MenuDto getItem(@PathVariable Integer id) {
        return menuService.getItem(id);
    }

    //  ADMIN and USER can view all items
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all")
    public List<MenuDto> getAllItems() {
        return menuService.getItems();
    }

    //  ADMIN and USER can search items
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/search/{input}")
    public List<MenuDto> searchItems(@PathVariable String input) {
        return menuService.getItems(input);
    }

    //  Only ADMIN can update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public MenuDto updateItem(@Valid @RequestBody MenuDto dto) {
        return menuService.updateItem(dto);
    }

    // Only ADMIN can delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Integer id) {
        menuService.deleteItem(id);
        return "Menu item deleted successfully";
    }
}
