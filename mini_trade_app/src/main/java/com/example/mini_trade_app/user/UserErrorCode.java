package com.example.mini_trade_app.user;

import com.example.mini_trade_app.shared.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {

    USER_INFO_MALFORM(
        "USER_000",
        "User Auth Header malform",
        400
    ),

    USER_NOT_FOUND(
        "USER_001",
        "User not found",
        404
    ),

    CONFLICT_WITH_USER(
        "USER_002",
        "User already have conflicting info",
        405
    ),

    WRONG_PASSWORD(
        "USER_003",
        "Mismatch email and password",
        403
    ),

    INSUFFICIENT_CREDENTIALS(
        "USER_004",
        "Insufficient credentials",
        403
    );

    private final String code;
    private final String message;
    private final Integer status;

    UserErrorCode(String code, String message, Integer Status) {
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
