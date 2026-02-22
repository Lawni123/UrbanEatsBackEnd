package com.urbanEats.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.urbanEats.dto.OrderDto;
import com.urbanEats.dto.OrderItemDto;
import com.urbanEats.dto.PricingResponseDto;
import com.urbanEats.entity.Cart;
import com.urbanEats.entity.CartItem;
import com.urbanEats.entity.Combo;
import com.urbanEats.entity.Menu;
import com.urbanEats.entity.Order;
import com.urbanEats.entity.OrderItem;
import com.urbanEats.entity.Payment;
import com.urbanEats.entity.User;
import com.urbanEats.enums.OrderStatus;
import com.urbanEats.enums.OrderType;
import com.urbanEats.enums.PaymentMethod;
import com.urbanEats.enums.PaymentStatus;
import com.urbanEats.exception.CartException;
import com.urbanEats.exception.ComboException;
import com.urbanEats.exception.MenuException;
import com.urbanEats.exception.OrderException;
import com.urbanEats.exception.PaymentException;
import com.urbanEats.exception.UserException;
import com.urbanEats.repo.CartRepo;
import com.urbanEats.repo.ComboRepo;
import com.urbanEats.repo.MenuRepo;
import com.urbanEats.repo.OrderRepo;
import com.urbanEats.repo.PaymentRepo;
import com.urbanEats.repo.UserRepo;
import com.urbanEats.request.OrderByComboRequest;
import com.urbanEats.request.OrderByMenuRequest;
import com.urbanEats.service.EmailService;
import com.urbanEats.service.OfferService;
import com.urbanEats.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MenuRepo menuRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ComboRepo comboRepo;

	@Autowired
	private OfferService offerService;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private EmailService emailService;

	private OrderDto toDto(Order entity) {
		OrderDto dto = new OrderDto();
		dto.setUserId(entity.getUser().getUserId());
		dto.setOrderId(entity.getOrderId());
		dto.setDiscountAmount(entity.getDiscountAmount());
		dto.setFinalAmount(entity.getFinalAmount());
		dto.setTotalAmount(entity.getTotalAmount());
		if (entity.getPayment() != null) {
			dto.setPaymentStatus(entity.getPayment().getPaymentStatus());
			dto.setPaymentMethod(entity.getPayment().getPaymentMethod());
		}
		dto.setOrderStatus(entity.getOrderStatus());
		dto.setOrderType(entity.getOrderType());
		List<OrderItemDto> oIList = entity.getOrderItems().stream().map(item -> {
			OrderItemDto oIdto = new OrderItemDto();
			oIdto.setMenuId(item.getMenu().getMenuId());
			oIdto.setOrderItemId(item.getOrderItemId());
			oIdto.setName(item.getMenu().getName());
			oIdto.setPrice(item.getPrice());
			oIdto.setQuantity(item.getQuantity());
			return oIdto;
		}).toList();

		dto.setOrderItemDto(oIList);

		return dto;
	}

	@Override
	public OrderDto createOrderFromMenu(Long userId, OrderByMenuRequest request) {

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found", HttpStatus.NOT_FOUND));

		Menu menu = menuRepo.findById(request.getMenuId())
				.orElseThrow(() -> new MenuException("Menu Item Not Found", HttpStatus.NOT_FOUND));

		PricingResponseDto pricing = offerService.getMenuPricing(menu);

		Order order = new Order();
		order.setUser(user);
		order.setOrderType(request.getOrderType());
		order.setOrderStatus(OrderStatus.UNPAID);

		OrderItem item = new OrderItem();
		item.setMenu(menu);
		item.setQuantity(request.getQuantity());
		item.setPrice(pricing.getFinalPrice());
		item.setOrder(order);

		double totalAmount = pricing.getOriginalPrice() * request.getQuantity();
		double discountAmount = pricing.getDiscountAmount() * request.getQuantity();
		double finalAmount = pricing.getFinalPrice() * request.getQuantity();

		order.setTotalAmount(totalAmount);
		order.setDiscountAmount(discountAmount);
		order.setFinalAmount(finalAmount);

		order.setOrderItems(Arrays.asList(item));
		Order savedOrder = orderRepo.save(order);

		return toDto(savedOrder);
	}

	@Override
	public OrderDto createOrderFromCombo(Long userId, OrderByComboRequest request) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found", HttpStatus.NOT_FOUND));

		Combo combo = comboRepo.findById(request.getComboId())
				.orElseThrow(() -> new ComboException("Combo Not Found", HttpStatus.NOT_FOUND));

		PricingResponseDto pricing = offerService.getComboPricing(combo);

		Order order = new Order();
		order.setUser(user);
		order.setOrderType(request.getOrderType());
		order.setOrderStatus(OrderStatus.UNPAID);

		List<OrderItem> oIList = combo.getComboItems().stream().map(cItem -> {
			OrderItem item = new OrderItem();
			item.setMenu(cItem.getMenu());
			item.setQuantity(cItem.getQuantity() * request.getQuantity());
			item.setPrice(cItem.getMenu().getPrice());
			item.setOrder(order);
			return item;
		}).toList();

		double totalAmount = pricing.getOriginalPrice() * request.getQuantity();
		double discountAmount = pricing.getDiscountAmount() * request.getQuantity();
		double finalAmount = pricing.getFinalPrice() * request.getQuantity();

		order.setTotalAmount(totalAmount);
		order.setDiscountAmount(discountAmount);
		order.setFinalAmount(finalAmount);

		order.setOrderItems(oIList);

		Order savedOrder = orderRepo.save(order);

		return toDto(savedOrder);
	}

	@Override
	public OrderDto createOrderFromCart(Long userId, OrderType orderType) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found", HttpStatus.NOT_FOUND));

		Cart cart = cartRepo.findByUserId(userId)
				.orElseThrow(() -> new CartException("Cart Not Found", HttpStatus.NOT_FOUND));

		Order order = new Order();
		order.setUser(user);
		order.setOrderType(orderType);
		order.setOrderStatus(OrderStatus.UNPAID);

		double totalAmount = 0.0;
		double discountAmount = 0.0;
		double finalAmount = 0.0;

		List<OrderItem> oIList = new ArrayList<>();
		for (CartItem cItem : cart.getCartItems()) {
			OrderItem item = new OrderItem();
			PricingResponseDto pricing = offerService.getMenuPricing(cItem.getMenu());

			item.setMenu(cItem.getMenu());
			item.setPrice(pricing.getOriginalPrice());
			item.setOrder(order);
			item.setQuantity(cItem.getQuantity());

			totalAmount += pricing.getOriginalPrice() * cItem.getQuantity();
			discountAmount += pricing.getDiscountAmount() * cItem.getQuantity();
			finalAmount += pricing.getFinalPrice() * cItem.getQuantity();

			oIList.add(item);
		}

		order.setTotalAmount(totalAmount);
		order.setDiscountAmount(discountAmount);
		order.setFinalAmount(finalAmount);

		order.setOrderItems(oIList);

		Order savedOrder = orderRepo.save(order);

		cart.getCartItems().clear();
		cartRepo.save(cart);

		return toDto(savedOrder);
	}

	@Override
	public OrderDto getOrderById(Long orderId) {

		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));
		return toDto(order);
	}

	@Override
	public List<OrderDto> getOrdersByUser(Long userId) {
		List<Order> oList = orderRepo.findByUserUserId(userId);

		return oList.stream().map(item -> toDto(item)).toList();
	}

	@Override
	public List<OrderDto> getAllOrders() {
		List<Order> oList = orderRepo.findAll();

		return oList.stream().map(item -> toDto(item)).toList();
	}

	@Override
	public void cancelOrder(Long userId, Long orderId) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));

		if (!order.getOrderStatus().equals(OrderStatus.PREPARING)
				|| !order.getOrderStatus().equals(OrderStatus.PREPARED)) {
			throw new OrderException("You Cannot Cancel the Order Once It is Started Preparing Or already prepared",
					HttpStatus.BAD_REQUEST);
		}
		if (!userId.equals(order.getUser().getUserId())) {
			throw new OrderException("Cant Cancel Any Others Orders", HttpStatus.UNAUTHORIZED);
		}
		order.setOrderStatus(OrderStatus.CANCELLED);

		orderRepo.save(order);
	}

	@Override
	public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {

		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));

		if (status == OrderStatus.PREPARING || status == OrderStatus.PREPARED) {

			Payment payment = order.getPayment();

			if (payment == null || payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
				throw new OrderException("please Wait The Customer Has Not Yet Done the Payment For This Order",
						HttpStatus.BAD_REQUEST);
			}
		}

		if (status == OrderStatus.PREPARED) {
			emailService.sendMail(order.getUser().getEmail(), "Your Order is Ready for Pickup",
					"Hello " + order.getUser().getName() + ",\n\n"
							+ "Great news! Your order has been prepared and is ready for pickup.\n\n" + "Order ID: "
							+ order.getOrderId() + "\n\n" + "Thank you for ordering with UrbanEats!\n\n"
							+ "UrbanEats Team");
		}

		order.setOrderStatus(status);
		return toDto(orderRepo.save(order));
	}

	@Override
	@Transactional
	public String initiatePayment(Long userId, Long orderId, PaymentMethod paymentMethod) {

		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));
		if (!userId.equals(order.getUser().getUserId())) {
			throw new OrderException("Cant Initiate Any Others Orders Payment", HttpStatus.UNAUTHORIZED);
		}
		if (order.getOrderStatus() != OrderStatus.UNPAID) {
			throw new OrderException("Order is already processed", HttpStatus.BAD_REQUEST);
		}

		if (order.getPayment() != null) {
			throw new PaymentException("Payment already initiated", HttpStatus.BAD_REQUEST);
		}

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentStatus(PaymentStatus.PENDING);
		payment.setPaymentMethod(paymentMethod);

		String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
		payment.setTransactionId(transactionId);

		paymentRepo.save(payment);

		order.setPayment(payment);
		orderRepo.save(order);

		return transactionId;
	}

	@Override
	@Transactional
	public String confirmPayment(Long userId, String transactionId, boolean success) {

		Payment payment = paymentRepo.findByTransactionId(transactionId)
				.orElseThrow(() -> new PaymentException("Invalid Transaction", HttpStatus.NOT_FOUND));
		if (!userId.equals(payment.getOrder().getUser().getUserId())) {
			throw new OrderException("Cant Confirm Any Others Orders Payment", HttpStatus.UNAUTHORIZED);
		}

		if (success) {
			payment.setPaymentStatus(PaymentStatus.SUCCESS);
			payment.getOrder().setOrderStatus(OrderStatus.CONFIRMED);
		} else {
			payment.setPaymentStatus(PaymentStatus.FAILED);
			payment.getOrder().setOrderStatus(OrderStatus.UNPAID);
		}

		paymentRepo.save(payment);
		orderRepo.save(payment.getOrder());

		return "Payment " + (success ? "Successful" : "Failed");
	}

}
