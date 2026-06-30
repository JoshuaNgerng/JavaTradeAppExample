package com.example.mini_trade_app.order;

import com.example.mini_trade_app.shared.exception.ErrorCode;

public enum OrderErrorCode implements ErrorCode {
    USER_MISMATCH(
        "ORDER_001",
        "Mismatch user",
        403
    ),
    USER_TYPE_MISMATCH(
        "ORDER_002",
        "Mismatch user role",
        403
    ),
    ORDER_ITEM_NOT_FOUND(
        "ORDER_003",
        "Order Item not found",
        404
    ),
    ORDER_NOT_FOUND(
        "ORDER_004",
        "Order not found",
        404
    ),
    ORDER_STATUS_LOCK(
        "ORDER_005",
        "Order already out of draft mode",
        403
    ),
    ORDER_EMPTY(
        "ORDER_006",
        "Order cannot checkout with no items",
        409
    ),
    ORDER_INVALID_STATE(
        "ORDER_007",
        "Order have an invalid state",
        500
    );

    private final String code;
    private final String message;
    private final Integer status;

    OrderErrorCode(String code, String message, Integer Status) {
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