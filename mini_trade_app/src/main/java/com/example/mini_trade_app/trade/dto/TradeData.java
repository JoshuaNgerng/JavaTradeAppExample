package com.example.mini_trade_app.trade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TradeData(
    long quantity,
    BigDecimal executionPricePerUnit,
    BigDecimal totalPrice,
    LocalDateTime executedAt
) {
}
