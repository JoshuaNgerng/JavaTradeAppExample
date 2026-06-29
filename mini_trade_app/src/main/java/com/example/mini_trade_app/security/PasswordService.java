package com.example.mini_trade_app.security;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Service;

import com.example.mini_trade_app.config.PasswordProperties;

@Service
public class PasswordService {

    private final int ITERATION;
    private final int KEY_LENGTH;

    public PasswordService(PasswordProperties config) {
        this.ITERATION = config.iteration();
        this.KEY_LENGTH = config.key_length();
    }

    // Generate salt
    public String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password
    public String hashPassword(String password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    this.ITERATION,
                    this.KEY_LENGTH
            );

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    // Verify password
    public boolean verify(String password, String salt, String expectedHash) {
        String hash = hashPassword(password, salt);
        return hash.equals(expectedHash);
    }
}
