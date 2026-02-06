package com.urbanEats.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.CartDto;
import com.urbanEats.dto.CartItemDto;
import com.urbanEats.entity.Cart;
import com.urbanEats.entity.CartItem;
import com.urbanEats.entity.Menu;
import com.urbanEats.exception.CartException;
import com.urbanEats.exception.MenuException;
import com.urbanEats.exception.UserException;
import com.urbanEats.repo.CartRepo;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.repo.UserRepo;
import com.urbanEats.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MenuRepo menuRepo;

	CartDto toDto(Cart cart) {
		CartDto cartDto = new CartDto();
		
		cartDto.setCartId(cart.getCartId());
		cartDto.setUserId(cart.getUser().getUserId());
		List<CartItemDto> cartItemList = new ArrayList<>();
		for(CartItem ci:cart.getCartItems()) {
			CartItemDto cIDto = new CartItemDto();
			cIDto.setCartItemId(ci.getCartItemId());
			cIDto.setMenuId(ci.getMenu().getMenuId());
			cIDto.setName(ci.getMenu().getName());
			cIDto.setImg(ci.getMenu().getImg());
			cIDto.setQuantity(ci.getQuantity());
			
			cartItemList.add(cIDto);
		}
		cartDto.setCartItems(cartItemList);
		
		return cartDto;
	}
	@Override
	public CartDto addItemToCart(Long userId, Long menuId) {

		Cart cart = cartRepo.findByUserId(userId).orElse(null);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(userRepo.findById(userId).orElseThrow(() -> new UserException("User Not Found",HttpStatus.NOT_FOUND)));
			cart.setCartItems(new ArrayList<>());
		}

		Menu menu = menuRepo.findById(menuId).orElseThrow(() -> new MenuException("Menu Item Not Found",HttpStatus.NOT_FOUND));

		Optional<CartItem> existingItem = cart.getCartItems().stream()
				.filter(ci -> ci.getMenu().getMenuId().equals(menuId)).findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(item.getQuantity() + 1);

		} else {
			CartItem cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setMenu(menu);
			cartItem.setQuantity(1);

			cart.getCartItems().add(cartItem);
		}

		cart=cartRepo.save(cart);
		return toDto(cart);
	}

	@Override
	public CartDto removeItemFromCart(Long userId, Long cartItemId) {

	    Cart cart = cartRepo.findByUserId(userId)
	            .orElseThrow(() -> new CartException("No Items In the Cart", HttpStatus.NOT_FOUND));

	    CartItem cartItem = cart.getCartItems()
	            .stream()
	            .filter(item -> item.getCartItemId().equals(cartItemId))
	            .findFirst()
	            .orElseThrow(() -> new CartException("Cart Item Not Found", HttpStatus.NOT_FOUND));

	    if (cartItem.getQuantity() > 1) {
	        cartItem.setQuantity(cartItem.getQuantity() - 1);
	    } else {
	        cart.getCartItems().remove(cartItem);
	    }

	     cart = cartRepo.save(cart);
	     
	     return toDto(cart);
	}


	@Override
	public CartDto getCartByUserId(Long userId) {
		
		CartDto cartDto = null;
		
		Cart cart = cartRepo.findByUserId(userId).orElse(null);
		if(cart==null) {
			return cartDto;
		}
		cartDto = toDto(cart);
		
		return cartDto;
	}

}
