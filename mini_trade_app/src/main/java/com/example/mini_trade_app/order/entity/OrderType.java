package com.example.mini_trade_app.order.entity;

import com.example.mini_trade_app.user.entity.RoleType;

public enum OrderType {
    BUYER,
    SELLER;

    private static final String PREFIX = "ROLE_";

    static public OrderType fromUserRole(RoleType role) {
        String role_str = role.toString().toUpperCase();
        if (role_str.startsWith(PREFIX)) {
            role_str = role_str.substring(PREFIX.length());
        }
        return OrderType.valueOf(role_str);
    }
}
