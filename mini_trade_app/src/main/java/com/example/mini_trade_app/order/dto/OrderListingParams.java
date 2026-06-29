package com.example.mini_trade_app.order.dto;

import java.util.Optional;

import com.example.mini_trade_app.order.entity.OrderStatus;
import com.example.mini_trade_app.shared.dto.ListingParams;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record OrderListingParams(
    @JsonUnwrapped ListingParams pageParams,
    Optional<OrderStatus> status
) {
}
