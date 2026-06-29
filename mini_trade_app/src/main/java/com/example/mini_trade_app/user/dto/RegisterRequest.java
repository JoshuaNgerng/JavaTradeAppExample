package com.example.mini_trade_app.user.dto;

public record RegisterRequest(
    String username,
    String email,
    String password,
    String role
) { }
