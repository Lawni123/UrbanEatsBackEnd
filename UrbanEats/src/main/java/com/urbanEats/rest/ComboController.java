package com.urbanEats.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanEats.exception.ComboException;
import com.urbanEats.request.ComboRequest;
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
		
		comboService.addCombo(comboRequest);
		
		return ResponseEntity.ok("Successfuly Combo Added");
	}
}
