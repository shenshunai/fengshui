package com.xuanxue.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用户资料实体 - 包含出生信息
 */
@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 出生日期
     */
    private LocalDate birthDate;
    
    /**
     * 出生时间
     */
    private LocalTime birthTime;
    
    /**
     * 出生地点
     */
    @Column(length = 100)
    private String birthPlace;
    
    /**
     * 是否为农历: 0-公历 1-农历
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer isLunar = 0;
    
    /**
     * 性别: 1-男 2-女
     */
    private Integer gender;
    
    /**
     * 星座
     */
    @Column(length = 20)
    private String zodiacSign;
    
    /**
     * 生肖
     */
    @Column(length = 10)
    private String chineseZodiac;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
