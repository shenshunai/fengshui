package com.xuanxue.config;

import com.platform.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration:86400000}") Long accessExpiration,
            @Value("${jwt.refresh-expiration:604800000}") Long refreshExpiration) {
        return new JwtUtil(secret, accessExpiration, refreshExpiration);
    }
}
