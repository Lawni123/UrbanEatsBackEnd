
package com.urbanEats.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanEats.dto.ComboDto;
import com.urbanEats.exception.ComboException;
import com.urbanEats.request.ComboRequest;
import com.urbanEats.response.ApiResponse;
import com.urbanEats.service.ComboService;
import com.urbanEats.service.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/combo/")
public class ComboController {

	@Autowired
	private ComboService comboService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/secure/add")
	public ResponseEntity<?> addCombo(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody ComboRequest comboRequest,
			BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			throw new ComboException("Invalid input", HttpStatus.BAD_REQUEST);
		}
		
		ComboDto comboDto = comboService.addCombo(comboRequest);
		ApiResponse<ComboDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfuly Combo Added");
		response.setData(comboDto);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/public/get/{id}")
	public ResponseEntity<?> getCombo(@PathVariable Long id){
		ComboDto comboDto = comboService.getCombo(id);
		ApiResponse<ComboDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched Combo");
		response.setData(comboDto);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/public/getCombo")
	public ResponseEntity<?> getCombos(@RequestParam String input){
		List<ComboDto> comboDtoList = comboService.getCombos(input);
		ApiResponse<List<ComboDto>> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched Combos");
		response.setData(comboDtoList);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/public/get")
	public ResponseEntity<?> getCombos(){
		List<ComboDto> comboDtoList = comboService.getCombos();
		ApiResponse<List<ComboDto>> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfully Fetched Combos");
		response.setData(comboDtoList);
		
		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/secure/update")
	public ResponseEntity<?> updateCombo(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody ComboRequest comboRequest,
			BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			throw new ComboException("Invalid input", HttpStatus.BAD_REQUEST);
		}
		
		ComboDto comboDto = comboService.updateCombo(comboRequest);
		ApiResponse<ComboDto> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfuly Combo Updated");
		response.setData(comboDto);
		return ResponseEntity.ok(response);
		
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/secure/delete/{id}")
	public ResponseEntity<?> deleteCombo(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long id){
		
		comboService.deleteCombo(id);
		  
		ApiResponse<String> response = new ApiResponse<>();
		response.setStatus("success");
		response.setMessage("Successfuly Combo Deleted");
		return ResponseEntity.ok(response);
	}
}
