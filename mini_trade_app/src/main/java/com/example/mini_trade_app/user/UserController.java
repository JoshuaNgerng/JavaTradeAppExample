package com.example.mini_trade_app.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.mini_trade_app.security.CustomUserPrincipal;
import com.example.mini_trade_app.security.JwtService;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.user.dto.LoginRequest;
import com.example.mini_trade_app.user.dto.AuthResponse;
import com.example.mini_trade_app.user.dto.RegisterRequest;
import com.example.mini_trade_app.user.dto.UserView;
import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService service;
    private final JwtService  jwtUtil;

	public UserController(
        UserService service, JwtService jwtUtil
    ) {
		this.service = service;
        this.jwtUtil = jwtUtil;
	}

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {
        User user = service.register(req);
        String token = this.jwtUtil.generateToken(
            user.getId(), user.getDefaultRole()
        );
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        User user = service.login(req);
        String token = this.jwtUtil.generateToken(
            user.getId(), user.getDefaultRole()
        );
        return new AuthResponse(token);
    }

    @GetMapping("/change_role/{role}")
    public AuthResponse changeRole(
        Authentication authentication, 
        @PathVariable String role
    ) {
        Long userId = Long.parseLong(authentication.getName());

        UserView user = service.getUserView(userId);

        RoleType role_ = RoleType.valueOf(role);

        if (!user.roles().contains(role_)) {
            throw new BusinessException(
                UserErrorCode.INSUFFICIENT_CREDENTIALS
            );
        }

        String token = this.jwtUtil.generateToken(
            userId, role_
        );

        return new AuthResponse(token);
    }

    @GetMapping("/profile")
    public UserView getUser(@AuthenticationPrincipal CustomUserPrincipal user) {
        return service.getUserView(user.getUserId());
    }
}
