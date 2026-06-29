package com.example.mini_trade_app.user.dto;

import java.util.Set;

import com.example.mini_trade_app.user.entity.RoleType;

public record UserView(
    String id,
    String userName,
    String email,
    Set<RoleType> roles
) { }
