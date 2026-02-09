package com.urbanEats.request;

import com.urbanEats.enums.OrderType;

import lombok.Data;

@Data
public class OrderRequestDto {

    private Long userId;

    private OrderType orderType;   // DINE_IN / TAKE_AWAY / ONLINE

    private Integer tableNumber;   // only for DINE_IN
}
