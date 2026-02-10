package com.urbanEats.service;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanEats.dto.OfferDto;

public interface OfferService {
	OfferDto addOffer(OfferDto offerDto);
	OfferDto getOffer(Integer id);
	List<OfferDto> getAllOffers();
	List<OfferDto> getOffersByDate(LocalDateTime presentDate);
	List<OfferDto> getOffersByPrice(Double Price);
	List<OfferDto> getOfferByName();
 }
