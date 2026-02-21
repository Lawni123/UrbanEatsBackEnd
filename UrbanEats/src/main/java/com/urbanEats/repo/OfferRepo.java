package com.urbanEats.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanEats.entity.Combo;
import com.urbanEats.entity.Menu;
import com.urbanEats.entity.Offer;

public interface OfferRepo extends JpaRepository<Offer, Long>{
	List<Offer> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start,LocalDate end);
	List<Offer> findByDiscountPercentageLessThanEqualOrFlatDiscountAmountLessThanEqual(Double price,Double price1);
	List<Offer> findByMenusContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
	        Menu menu, LocalDate start, LocalDate end);

	List<Offer> findByCombosContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
	        Combo combo, LocalDate start, LocalDate end);
	


}
