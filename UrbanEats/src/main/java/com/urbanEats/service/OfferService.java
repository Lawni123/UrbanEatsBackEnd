package com.urbanEats.service;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanEats.dto.OfferDto;
import com.urbanEats.dto.PricingResponseDto;
import com.urbanEats.entity.Combo;
import com.urbanEats.entity.Menu;

public interface OfferService {
	OfferDto addOffer(OfferDto offerDto);
	OfferDto getOffer(Integer id);
	List<OfferDto> getAllOffers();
	List<OfferDto> getOffersByDate(LocalDateTime presentDate);
	OfferDto updateOffer(OfferDto offerDto);
	void deleteOffer(Integer id);
	
	void applyOfferToMenus(Long offerId, List<Long> menuIds);
	void applyOfferToCombos(Long offerId, List<Long> comboIds);
	Double calculateDiscountedPriceForMenu(Long menuId);
	Double calculateDiscountedPriceForCombo(Long comboId);
	PricingResponseDto getMenuPricing(Menu menu);
	PricingResponseDto getComboPricing(Combo combo);
 }
