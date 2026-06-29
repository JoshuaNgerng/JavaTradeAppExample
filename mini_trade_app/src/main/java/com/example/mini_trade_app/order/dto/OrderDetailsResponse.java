package com.example.mini_trade_app.order.dto;

import java.util.List;

import com.example.mini_trade_app.trade.dto.TradeData;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record OrderDetailsResponse(
    @JsonUnwrapped CreateOrderResponse order,
    List<TradeData> trades
) {
}
