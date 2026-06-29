package com.example.mini_trade_app.trade.dto;

import java.util.List;

import com.example.mini_trade_app.shared.dto.Listing;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record TradeLisitingResponse(
    @JsonUnwrapped Listing stats,
    List<TradeData> data
) {
}
