package com.example.mini_trade_app.security;

import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.user.entity.RoleType;

public record AuthUserInfo(
        User user,
        RoleType role
) {
}
