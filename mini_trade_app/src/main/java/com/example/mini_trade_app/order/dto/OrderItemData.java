package com.example.mini_trade_app.order.dto;

import java.math.BigDecimal;

import com.example.mini_trade_app.product.Product;

public record OrderItemData(
    Product product,
    long quantity,
    BigDecimal pricePerUnit,
    long remainingQuantity
) implements OrderItemShape {
}
