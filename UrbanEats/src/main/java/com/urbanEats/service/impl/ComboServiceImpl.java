package com.urbanEats.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.urbanEats.entity.Combo;
import com.urbanEats.entity.ComboItem;
import com.urbanEats.entity.Menu;
import com.urbanEats.repo.ComboItemRepo;
import com.urbanEats.repo.ComboRepo;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.request.ComboItemRequest;
import com.urbanEats.request.ComboRequest;
import com.urbanEats.service.ComboService;

public class ComboServiceImpl implements ComboService{

	@Autowired
	private ComboRepo comboRepo;
	
	@Autowired
	private ComboItemRepo comboItemRepo;
	
	@Autowired
	private MenuRepo menuRepo;
	
	@Override
	public void addCombo(ComboRequest request) {
		Combo combo = new Combo();
		combo.setComboName(request.getComboName());
		combo.setImg(request.getImg());
		
		Double totalPrice=0.0;
		
		List<ComboItem> cIlist = new ArrayList<>();
		for(ComboItemRequest ci : request.getComboItems()) {
			ComboItem comboItem = new ComboItem();
			comboItem.setCombo(combo);
			
			Menu menu = menuRepo.findById(ci.getMenuId()).get();
			comboItem.setMenu(menu);
			comboItem.setQuantity(ci.getQuantity());
			
			cIlist.add(comboItem);
			
			totalPrice += menu.getPrice();
		}
		
		combo.setComboPrice(totalPrice);
		System.out.println(comboRepo.save(combo));
		System.out.println(comboItemRepo.saveAll(cIlist)); 
	}

	@Override
	public ComboRequest getCombo(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboRequest> getCombos(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboRequest> getCombos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComboRequest updateCombo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCombo(Integer id) {
		// TODO Auto-generated method stub
		
	}

}
