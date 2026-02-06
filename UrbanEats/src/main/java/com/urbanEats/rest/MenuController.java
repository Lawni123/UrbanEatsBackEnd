package com.urbanEats.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.urbanEats.dto.MenuDto;
import com.urbanEats.exception.MenuException;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.CustomUserDetails;
import com.urbanEats.service.MenuService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	// Only ADMIN can add menu item
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/secure/add")
	public ResponseEntity<?> addItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody MenuDto dto
			,BindingResult result) {
		if(result.hasErrors()) {
			throw new MenuException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		MenuDto menuDto = menuService.addItem(dto);

		ApiResponse<MenuDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Added MenuItem");
		response.setData(menuDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// All USERs can view single item
	@GetMapping("/secure/get/{id}")
	public ResponseEntity<?>  getItem(@PathVariable Integer id) {

		MenuDto menuDto = menuService.getItem(id);
		ApiResponse<MenuDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched MenuItem");
		response.setData(menuDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// All USERs can view all items
	@GetMapping("/secure/all")
	public ResponseEntity<?> getAllItems() {
		List<MenuDto> menuDtoList = menuService.getItems();
		ApiResponse<List<MenuDto>> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched MenuItems");
		response.setData(menuDtoList);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// All USERs can search items
	@GetMapping("/secure/search/{input}")
	public ResponseEntity<?> searchItems(@PathVariable String input) {
		List<MenuDto> menuDtoList = menuService.getItems(input);
		ApiResponse<List<MenuDto>> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Searched MenuItems");
		response.setData(menuDtoList);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// Only ADMIN can update
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/secure/update")
	public ResponseEntity<?> updateItem(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody MenuDto dto
			,BindingResult result) {
		if(result.hasErrors()) {
			throw new MenuException("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		MenuDto menuDto = menuService.updateItem(dto);
		ApiResponse<MenuDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Updated MenuItem");
		response.setData(menuDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// Only ADMIN can delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/secure/delete/{id}")
	public ResponseEntity<?> deleteItem(
			@AuthenticationPrincipal CustomUserDetails customUserDetails
			, @PathVariable Integer id) {
		menuService.deleteItem(id);
		
		ApiResponse<String> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Deleted MenuItem");
		
		return ResponseEntity.ok(response);
	}
}