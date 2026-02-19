package com.urbanEats.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Offer;

public interface OfferRepo extends JpaRepository<Offer, Long>{
	List<Offer> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start,LocalDate end);
	List<Offer> findByDiscountPercentageLessThanEqual(Double price);
}
