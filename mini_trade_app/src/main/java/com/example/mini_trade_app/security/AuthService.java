package com.example.mini_trade_app.security;

import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.user.UserErrorCode;
import com.example.mini_trade_app.user.UserRepository;

public class AuthService {
    private final UserRepository users;
    
    public AuthService(UserRepository users) {
        this.users = users;
    }

    public AuthUserInfo verifyUserInfo(CustomUserPrincipal principal) {
        User user = users.findById(principal.getUserId()).orElseThrow(
            () -> new BusinessException(UserErrorCode.USER_NOT_FOUND)
        );
        
        if (!user.getRoles().contains(principal.getActiveRole())) {
            throw new BusinessException(UserErrorCode.INSUFFICIENT_CREDENTIALS);
        }

        return new AuthUserInfo(user, principal.getActiveRole());
    }

}
