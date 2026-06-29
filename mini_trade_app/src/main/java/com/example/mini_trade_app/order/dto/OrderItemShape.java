package com.example.mini_trade_app.order.dto;

import java.math.BigDecimal;

import com.example.mini_trade_app.product.Product;

public interface OrderItemShape {
    Product product();
    long quantity();
    BigDecimal pricePerUnit();

    default BigDecimal totalPrice() {
        return this.pricePerUnit().multiply(
            BigDecimal.valueOf(this.quantity())
        );
    }
}
