package com.example.mini_trade_app.shared.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    String code,
    String message,
    LocalDateTime timestamp
) {
    public ApiErrorResponse(
        String code, String message
    ) {
        this(code, message, LocalDateTime.now());
    }
}