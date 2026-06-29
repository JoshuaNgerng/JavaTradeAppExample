package com.example.mini_trade_app.order.dto;

import java.util.List;

import com.example.mini_trade_app.order.entity.OrderType;

public record CreateOrderForm(
    OrderType intent,
    List<OrderItemData> items
) {
}
