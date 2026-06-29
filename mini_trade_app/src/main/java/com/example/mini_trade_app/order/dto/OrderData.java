package com.example.mini_trade_app.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.mini_trade_app.order.entity.OrderStatus;

public record OrderData (
    OrderStatus status,
    BigDecimal totalPrice,
    List<OrderItemData> items
) { 
    
}
