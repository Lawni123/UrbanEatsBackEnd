package com.urbanEats.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.ComboDto;
import com.urbanEats.dto.ComboItemDto;
import com.urbanEats.dto.PricingResponseDto;
import com.urbanEats.entity.Combo;
import com.urbanEats.entity.ComboItem;
import com.urbanEats.entity.Menu;
import com.urbanEats.exception.ComboException;
import com.urbanEats.exception.MenuException;
import com.urbanEats.repo.ComboItemRepo;
import com.urbanEats.repo.ComboRepo;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.request.ComboItemRequest;
import com.urbanEats.request.ComboRequest;
import com.urbanEats.service.ComboService;
import com.urbanEats.service.OfferService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComboServiceImpl implements ComboService{

	@Autowired
	private ComboRepo comboRepo;
	
	@Autowired
	private ComboItemRepo comboItemRepo;
	
	@Autowired
    private OfferService offerService;
	
	@Autowired
	private MenuRepo menuRepo;
	
	 ComboDto toDto(Combo combo) {
		ComboDto comboDto = new ComboDto();
		comboDto.setComboId(combo.getComboId());
		comboDto.setComboName(combo.getComboName());
		comboDto.setComboPrice(combo.getComboPrice());
		comboDto.setImage(combo.getImg());
		
		List<ComboItemDto> cIlist = new ArrayList<>();
		
		for(ComboItem ci : combo.getComboItems()) {
			ComboItemDto comboItemDto = new ComboItemDto();
			comboItemDto.setComboItemId(ci.getComboItemId());
			comboItemDto.setMenuId(ci.getMenu().getMenuId());
			comboItemDto.setItemName(ci.getMenu().getName());
			comboItemDto.setQuantity(ci.getQuantity());
			
			cIlist.add(comboItemDto);
		}
		
		comboDto.setItems(cIlist);
		
		return comboDto;
	}
	
	Combo toEntity(ComboRequest request) {
		Combo combo = new Combo();
		combo.setComboName(request.getComboName());
		combo.setImg(request.getImg());

		Double totalPrice = 0.0;

		List<ComboItem> comboItems = new ArrayList<>();

		for (ComboItemRequest ci : request.getComboItems()) {

		    Menu menu = menuRepo.findById(ci.getMenuId())
		            .orElseThrow(() -> new MenuException("Menu not found",HttpStatus.NOT_FOUND));

		    ComboItem comboItem = new ComboItem();
		    comboItem.setCombo(combo);
		    comboItem.setMenu(menu);
		    comboItem.setQuantity(ci.getQuantity());

		    comboItems.add(comboItem);

		    totalPrice += menu.getPrice() * ci.getQuantity();
		}

		combo.setComboPrice(totalPrice);
		combo.setComboItems(comboItems);
		return combo;
	}
	
	@Override
	public ComboDto addCombo(ComboRequest request) {
		Combo combo = toEntity(request);
		combo = comboRepo.save(combo);
		return toDto(combo);
	}

	@Override
	public ComboDto getCombo(Long id) {
		Combo combo = comboRepo.findById(id)
				.orElseThrow(()->new ComboException("Combo Not Found", HttpStatus.NOT_FOUND));
		
		ComboDto comboDto = toDto(combo);
		
		PricingResponseDto pricing = offerService.getComboPricing(combo);

		comboDto.setOriginalPrice(pricing.getOriginalPrice());
		comboDto.setFinalPrice(pricing.getFinalPrice());
		comboDto.setDiscountAmount(pricing.getDiscountAmount());
		comboDto.setOfferApplied(pricing.getOfferApplied());
		comboDto.setOfferTitle(pricing.getOfferTitle());
		
		return comboDto;
	}

	@Override
	public List<ComboDto> getCombos(String input) {
		// TODO Auto-generated method stub
		List<Combo> ComboDtoList = comboRepo.findByComboNameContainingIgnoreCase(input);
		List<ComboDto> cDtoList = new ArrayList<>();
		for(Combo combo : ComboDtoList) {
			
			ComboDto comboDto = toDto(combo);
			
			PricingResponseDto pricing = offerService.getComboPricing(combo);

			comboDto.setOriginalPrice(pricing.getOriginalPrice());
			comboDto.setFinalPrice(pricing.getFinalPrice());
			comboDto.setDiscountAmount(pricing.getDiscountAmount());
			comboDto.setOfferApplied(pricing.getOfferApplied());
			comboDto.setOfferTitle(pricing.getOfferTitle());
			
			cDtoList.add(comboDto);
		}
		return cDtoList;
	}

	@Override
	public List<ComboDto> getCombos() {
		List<Combo> clist = comboRepo.findAll();
		
		List<ComboDto> cDtoList = new ArrayList<>();
		for(Combo combo : clist) {
			
			ComboDto comboDto = toDto(combo);
			
			PricingResponseDto pricing = offerService.getComboPricing(combo);

			comboDto.setOriginalPrice(pricing.getOriginalPrice());
			comboDto.setFinalPrice(pricing.getFinalPrice());
			comboDto.setDiscountAmount(pricing.getDiscountAmount());
			comboDto.setOfferApplied(pricing.getOfferApplied());
			comboDto.setOfferTitle(pricing.getOfferTitle());
			
			cDtoList.add(comboDto);
		}
		return cDtoList;
	}

	@Override
	public ComboDto updateCombo(ComboRequest request) {
		Combo combo = comboRepo.findById(request.getComboId())
				.orElseThrow(()->new ComboException("Combo Not Found", HttpStatus.NOT_FOUND));
		
		if (request.getComboName() != null) {
	        combo.setComboName(request.getComboName());
	    }

	    if (request.getImg() != null) {
	        combo.setImg(request.getImg());
	    }
	
        
	    if (request.getComboItems() != null) {

	        comboItemRepo.deleteAll(combo.getComboItems());
	        combo.getComboItems().clear();

	        List<ComboItem> newItems = new ArrayList<>();
	        Double price = 0.0;
	        for (ComboItemRequest itemRequest : request.getComboItems()) {

	            Menu menu = menuRepo.findById(itemRequest.getMenuId())
	                    .orElseThrow(() -> new MenuException("Menu Not Found",HttpStatus.NOT_FOUND));

	            ComboItem comboItem = new ComboItem();
	            comboItem.setCombo(combo);
	            comboItem.setMenu(menu);
	            comboItem.setQuantity(itemRequest.getQuantity());

	            newItems.add(comboItem);
	            price+=menu.getPrice();
	        }
	        combo.setComboPrice(price);
	        combo.setComboItems(newItems);
	    }
	    
	   combo= comboRepo.save(combo);
	   
	   return toDto(combo);
	}

	@Override
	public void deleteCombo(Long id) {
		if(!comboRepo.existsById(id)) {
			throw new ComboException("Combo Not Found", HttpStatus.NOT_FOUND);
		}
		
		comboRepo.deleteById(id);
	}

}
