package com.urbanEats.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "offers")
@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    private String title;
    private String description;

    private Double discountPercentage;
    private Double flatDiscountAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive = true;

    // Menu Level Offers
    @ManyToMany
    @JoinTable(
        name = "offer_menu",
        joinColumns = @JoinColumn(name = "offer_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<Menu> menus;

  

    // Combo Level Offers
    @ManyToMany
    @JoinTable(
        name = "offer_combo",
        joinColumns = @JoinColumn(name = "offer_id"),
        inverseJoinColumns = @JoinColumn(name = "combo_id")
    )
    private List<Combo> combos;
}

