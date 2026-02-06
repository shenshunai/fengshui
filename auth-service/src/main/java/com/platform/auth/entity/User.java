package com.platform.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体（鉴权服务持有）
 */
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String avatar;

    private LocalDateTime vipExpireTime;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isVip() {
        return vipExpireTime != null && vipExpireTime.isAfter(LocalDateTime.now());
    }
}
