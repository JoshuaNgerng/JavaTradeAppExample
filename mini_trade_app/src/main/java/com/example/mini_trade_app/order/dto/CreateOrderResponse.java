package com.example.mini_trade_app.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderResponse(
    long id,
    BigDecimal totalPrice,
    List<OrderItemData> items
) {
}
