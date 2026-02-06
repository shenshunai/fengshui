package com.platform.auth.service;

import com.platform.auth.dto.LoginRequest;
import com.platform.auth.dto.LoginResponse;
import com.platform.auth.dto.RegisterRequest;
import com.platform.auth.entity.User;
import com.platform.auth.repository.UserRepository;
import com.platform.common.exception.BusinessException;
import com.platform.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("手机号已注册");
        }
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(generateUsername(request.getPhone()));
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .isVip(user.isVip())
                .build();
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException("刷新令牌无效");
        }
        if (!"refresh".equals(jwtUtil.getTokenType(refreshToken))) {
            throw new BusinessException("令牌类型错误");
        }
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .isVip(user.isVip())
                .build();
    }

    private String generateUsername(String phone) {
        return "用户" + phone.substring(phone.length() - 4);
    }
}
