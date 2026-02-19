package com.urbanEats.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.urbanEats.dto.OfferDto;
import com.urbanEats.service.OfferService;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public OfferDto addOffer(@RequestBody OfferDto dto) {
        return offerService.addOffer(dto);
    }

    @GetMapping("/{id}")
    public OfferDto getOffer(@PathVariable Integer id) {
        return offerService.getOffer(id);
    }

    @GetMapping
    public List<OfferDto> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/active")
    public List<OfferDto> getOffersByDate() {
        return offerService.getOffersByDate(LocalDateTime.now());
    }

    @GetMapping("/price/{price}")
    public List<OfferDto> getOffersByPrice(@PathVariable Double price) {
        return offerService.getOffersByPrice(price);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public OfferDto updateOffer(@RequestBody OfferDto dto) {
        return offerService.updateOffer(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOffer(@PathVariable Integer id) {
        offerService.deleteOffer(id);
    }
}
