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
import com.urbanEats.entity.Offer;
import com.urbanEats.exception.OfferException;
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
    public List<OfferDto> getOffersByPrice(Double price) {
        return offerRepo
                .findByDiscountPercentageLessThanEqual(price)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferDto> getOfferByName() {
        return offerRepo.findAll()
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
}
