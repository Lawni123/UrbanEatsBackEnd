package com.urbanEats.entity;

import java.util.List;

import com.urbanEats.enums.FoodType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "menu")
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private FoodType foodType; // VEG, NON_VEG

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "menu")
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "menu")
    private List<OrderItem> orderItems;
    
    @Size(max = 5000)
    private String img;
    
    @ManyToMany(mappedBy = "menus")
    private List<Offer> offers;
}
