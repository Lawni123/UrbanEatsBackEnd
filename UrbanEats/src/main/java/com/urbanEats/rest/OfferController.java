package com.urbanEats.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.urbanEats.dto.OfferDto;
import com.urbanEats.service.OfferService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/secure/add")
    public OfferDto addOffer(@RequestBody OfferDto dto) {
        return offerService.addOffer(dto);
    }

    @GetMapping("/public/get/{id}")
    public OfferDto getOffer(@PathVariable Integer id) {
        return offerService.getOffer(id);
    }

    @GetMapping("/public/get")
    public List<OfferDto> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/public/active")
    public List<OfferDto> getOffersByDate() {
        return offerService.getOffersByDate(LocalDateTime.now());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/secure/update")
    public OfferDto updateOffer(@RequestBody OfferDto dto) {
        return offerService.updateOffer(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/secure/delete/{id}")
    public void deleteOffer(@PathVariable Integer id) {
        offerService.deleteOffer(id);
    }
    
    @PostMapping("/secure/{offerId}/apply-to-menus")
    public void applyOfferToMenus(
            @PathVariable Long offerId,
            @RequestBody List<Long> menuIds) {

        offerService.applyOfferToMenus(offerId, menuIds);
    }
    
    @PostMapping("/secure/{offerId}/apply-to-combos")
    public void applyOfferToCombos(
            @PathVariable Long offerId,
            @RequestBody List<Long> comboIds) {

        offerService.applyOfferToCombos(offerId, comboIds);
    }
}
