package com.example.mini_trade_app.user;

import org.springframework.stereotype.Service;

import com.example.mini_trade_app.security.PasswordService;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.user.dto.LoginRequest;
import com.example.mini_trade_app.user.dto.RegisterRequest;
import com.example.mini_trade_app.user.dto.UserView;
import com.example.mini_trade_app.user.entity.User;
import com.example.mini_trade_app.user.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository    repo;
    private final PasswordService   passwordService;
    private final UserMapper        userMapper;

    public User register(RegisterRequest req) {
        repo.findByEmail(req.email()).orElseThrow(
            () -> new BusinessException(UserErrorCode.CONFLICT_WITH_USER)
        );

        String salt = passwordService.generateSalt();
        String hash = passwordService.hashPassword(req.password(), salt);

        User user = User.newUser(
            req.username(), req.email(), salt, hash, req.role()
        );

        return repo.save(user);
    }

    public User login(LoginRequest req) {
        User user = repo.findByEmail(req.email()).orElseThrow(
            () -> new BusinessException(UserErrorCode.USER_NOT_FOUND)
        );
        boolean check = passwordService.verify(
            req.password(),
            user.getSaltBase64(),
            user.getHashBase64()
        );
        if (!check) {
            throw new BusinessException(UserErrorCode.WRONG_PASSWORD);
        }
        return user;
    }

    public UserView getUserView(long userId) {
        return userMapper.toView(repo.findById(userId).orElseThrow(
            () -> new BusinessException(UserErrorCode.USER_NOT_FOUND)
        ));
    }
}
