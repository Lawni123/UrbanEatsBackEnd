package com.urbanEats.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "combos")
@Data
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comboId;

    private String comboName;
    private double comboPrice;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    private List<ComboItem> comboItems;
    
    @Size(max = 5000)
    private String img;
    
    @ManyToMany(mappedBy = "combos")
    private List<Offer> offers;
}
