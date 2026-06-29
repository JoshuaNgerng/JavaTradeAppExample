package com.example.mini_trade_app.config;

import java.time.Duration;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    Duration expiration,
    String secret
) {}

/*
complciated validation example
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    String expiration,
    String secret
) {

    private final long expirationSeconds;

    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret cannot be empty");
        }

        this.expirationSeconds = parse(expiration);
    }

    public long expirationSeconds() {
        return expirationSeconds;
    }

    // specfic validation logic here 
    private long parse(String value) {
        value = value.toLowerCase().trim();

        if (value.endsWith("h")) {
            return Long.parseLong(value.replace("h", "")) * 3600;
        }
        if (value.endsWith("m")) {
            return Long.parseLong(value.replace("m", "")) * 60;
        }
        if (value.endsWith("s")) {
            return Long.parseLong(value.replace("s", ""));
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expiration format: " + value);
        }
    }
}
*/