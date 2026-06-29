package com.example.mini_trade_app.trade;

import com.example.mini_trade_app.shared.exception.ErrorCode;

public enum TradeErrorCode implements ErrorCode {
    TRADE_NOT_FOUND(
        "TRADE_001",
        "Trade not found",
        404
    );

    private final String code;
    private final String message;
    private final Integer status;

    TradeErrorCode(String code, String message, Integer Status) {
        this.code = code;
        this.message = message;
        this.status = Status; 
    }

    @Override
    public Integer status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
