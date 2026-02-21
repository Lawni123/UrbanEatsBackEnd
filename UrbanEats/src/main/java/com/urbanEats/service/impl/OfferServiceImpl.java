package com.urbanEats.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.OfferDto;
import com.urbanEats.dto.PricingResponseDto;
import com.urbanEats.entity.Combo;
import com.urbanEats.entity.Menu;
import com.urbanEats.entity.Offer;
import com.urbanEats.exception.OfferException;
import com.urbanEats.repo.ComboRepo;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.repo.OfferRepo;
import com.urbanEats.service.OfferService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepo offerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private ComboRepo comboRepo;


    private OfferDto toDto(Offer offer) {
        return modelMapper.map(offer, OfferDto.class);
    }

    private Offer toEntity(OfferDto dto) {
        Offer offer = modelMapper.map(dto, Offer.class);
        offer.setIsActive(true);
        return offer;
    }


    @Override
    public OfferDto addOffer(OfferDto offerDto) {
        Offer offer = toEntity(offerDto);
        offer = offerRepo.save(offer);
        return toDto(offer);
    }

    @Override
    public OfferDto getOffer(Integer id) {
        Offer offer = offerRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new OfferException("Offer Not Found", HttpStatus.NOT_FOUND));
        return toDto(offer);
    }

    @Override
    public List<OfferDto> getAllOffers() {
        return offerRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferDto> getOffersByDate(LocalDateTime presentDate) {
        LocalDate today = presentDate.toLocalDate();

        return offerRepo
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OfferDto updateOffer(OfferDto offerDto) {

        Offer offer = offerRepo.findById(offerDto.getId())
                .orElseThrow(() -> new OfferException("Offer Not Found", HttpStatus.NOT_FOUND));

        modelMapper.map(offerDto, offer);
        offer = offerRepo.save(offer);

        return toDto(offer);
    }

    @Override
    public void deleteOffer(Integer id) {
        if (!offerRepo.existsById(Long.valueOf(id))) {
            throw new OfferException("Offer Not Found", HttpStatus.NOT_FOUND);
        }
        offerRepo.deleteById(Long.valueOf(id));
    }


    @Override
    public void applyOfferToMenus(Long offerId, List<Long> menuIds) {

        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new OfferException("Offer Not Found", HttpStatus.NOT_FOUND));

        List<Menu> menus = menuRepo.findAllById(menuIds);

        if (menus.isEmpty()) {
            throw new OfferException("Menus Not Found", HttpStatus.NOT_FOUND);
        }

        offer.setMenus(menus);
        offerRepo.save(offer);
    }

    @Override
    public void applyOfferToCombos(Long offerId, List<Long> comboIds) {

        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new OfferException("Offer Not Found", HttpStatus.NOT_FOUND));

        List<Combo> combos = comboRepo.findAllById(comboIds);

        if (combos.isEmpty()) {
            throw new OfferException("Combos Not Found", HttpStatus.NOT_FOUND);
        }

        offer.setCombos(combos);
        offerRepo.save(offer);
    }


    @Override
    public Double calculateDiscountedPriceForMenu(Long menuId) {

        Menu menu = menuRepo.findById(menuId)
                .orElseThrow(() -> new OfferException("Menu Not Found", HttpStatus.NOT_FOUND));

        PricingResponseDto pricing = getMenuPricing(menu);
        return pricing.getFinalPrice();
    }

    @Override
    public Double calculateDiscountedPriceForCombo(Long comboId) {

        Combo combo = comboRepo.findById(comboId)
                .orElseThrow(() -> new OfferException("Combo Not Found", HttpStatus.NOT_FOUND));

        PricingResponseDto pricing = getComboPricing(combo);
        return pricing.getFinalPrice();
    }

	@Override
	public PricingResponseDto getComboPricing(Combo combo) {
		
        PricingResponseDto response = new PricingResponseDto();
        Double originalPrice = combo.getComboPrice();

        response.setOriginalPrice(originalPrice);

        List<Offer> validOffers = getValidActiveOffersForCombo(combo);

        if (validOffers.isEmpty()) {
            response.setFinalPrice(originalPrice);
            response.setDiscountAmount(0.0);
            response.setOfferApplied(false);
            response.setOfferTitle(null);
            return response;
        }

        Offer bestOffer = null;
        Double maxDiscount = 0.0;

        for (Offer offer : validOffers) {

            Double discount = calculateDiscountAmount(originalPrice, offer);

            if (discount > maxDiscount) {
                maxDiscount = discount;
                bestOffer = offer;
            }
        }

        response.setFinalPrice(originalPrice - maxDiscount);
        response.setDiscountAmount(maxDiscount);
        response.setOfferApplied(true);
        response.setOfferTitle(bestOffer.getTitle());

        return response;
	}
    @Override
    public PricingResponseDto getMenuPricing(Menu menu) {

        PricingResponseDto response = new PricingResponseDto();
        Double originalPrice = menu.getPrice();

        response.setOriginalPrice(originalPrice);

        List<Offer> validOffers = getValidActiveOffersForMenu(menu);

        if (validOffers.isEmpty()) {
            response.setFinalPrice(originalPrice);
            response.setDiscountAmount(0.0);
            response.setOfferApplied(false);
            response.setOfferTitle(null);
            return response;
        }

        Offer bestOffer = null;
        Double maxDiscount = 0.0;

        for (Offer offer : validOffers) {

            Double discount = calculateDiscountAmount(originalPrice, offer);

            if (discount > maxDiscount) {
                maxDiscount = discount;
                bestOffer = offer;
            }
        }

        response.setFinalPrice(originalPrice - maxDiscount);
        response.setDiscountAmount(maxDiscount);
        response.setOfferApplied(true);
        response.setOfferTitle(bestOffer.getTitle());

        return response;
    }


    private List<Offer> getValidActiveOffersForMenu(Menu menu) {

        LocalDate today = LocalDate.now();

        return offerRepo
                .findByMenusContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        menu, today, today)
                .stream()
                .filter(Offer::getIsActive)
                .collect(Collectors.toList());
    }

    private List<Offer> getValidActiveOffersForCombo(Combo combo) {

        LocalDate today = LocalDate.now();

        return offerRepo
                .findByCombosContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        combo, today, today)
                .stream()
                .filter(Offer::getIsActive)
                .collect(Collectors.toList());
    }

    private Double calculateDiscountAmount(Double price, Offer offer) {

        if (offer.getDiscountPercentage() != null) {
            return price * offer.getDiscountPercentage() / 100;
        }

        if (offer.getFlatDiscountAmount() != null) {
            return Math.min(offer.getFlatDiscountAmount(), price);
        }

        return 0.0;
    }




}