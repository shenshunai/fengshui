package com.platform.auth.controller;

import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.LoginResponse;
import com.platform.auth.dto.RegisterRequest;
import com.platform.auth.entity.User;
import com.platform.auth.service.AuthService;
import com.platform.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return Result.success("注册成功", user);
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return Result.success(response);
    }
}
