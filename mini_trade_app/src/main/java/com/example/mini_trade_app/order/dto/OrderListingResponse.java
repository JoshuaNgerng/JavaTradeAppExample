package com.example.mini_trade_app.order.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

import com.example.mini_trade_app.shared.dto.Listing;

public record OrderListingResponse(
    @JsonUnwrapped Listing stat,
    List<OrderData> data
) {
}