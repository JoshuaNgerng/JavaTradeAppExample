package com.example.mini_trade_app.security;

import com.example.mini_trade_app.user.entity.RoleType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CustomUserPrincipal {
    private Long userId;
    private RoleType activeRole;
}
