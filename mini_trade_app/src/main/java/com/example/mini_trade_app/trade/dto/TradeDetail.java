package com.example.mini_trade_app.trade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.mini_trade_app.product.Product;

public record TradeDetail(
    long quantity,
    BigDecimal executionPricePerUnit,
    BigDecimal totalPrice,
    LocalDateTime executedAt,
    Product product
) {
}
