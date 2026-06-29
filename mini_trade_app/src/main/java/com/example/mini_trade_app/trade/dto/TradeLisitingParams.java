package com.example.mini_trade_app.trade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.mini_trade_app.shared.dto.ListingParams;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record TradeLisitingParams (
    @JsonUnwrapped ListingParams pageParams,
    Optional<Long> upperQuantityLimit,
    Optional<Long> lowerQuantityLimit,
    Optional<BigDecimal> upperPricePerUnit,
    Optional<BigDecimal> lowerPricePerUnit,
    Optional<LocalDateTime> beforeDate,
    Optional<LocalDateTime> afterDate
) { }