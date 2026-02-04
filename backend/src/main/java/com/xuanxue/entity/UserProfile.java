package com.xuanxue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用户资料实体 - 包含出生信息
 */
@TableName("user_profiles")
@Data
public class UserProfile {

    @TableId
    private Long id;

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
    private String birthPlace;

    /**
     * 是否为农历: 0-公历 1-农历
     */
    private Integer isLunar = 0;

    /**
     * 性别: 1-男 2-女
     */
    private Integer gender;

    /**
     * 星座
     */
    private String zodiacSign;

    /**
     * 生肖
     */
    private String chineseZodiac;

    private LocalDateTime createdAt;
}
