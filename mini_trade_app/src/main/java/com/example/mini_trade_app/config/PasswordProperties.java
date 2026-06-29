package com.example.mini_trade_app.config;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "app.password")
public record PasswordProperties (
    int iteration,
    int key_length
) {}
