package com.example.mini_trade_app.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public Long getCurrentUserId() {
        String id = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return Long.parseLong(id);
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();
    }
}
