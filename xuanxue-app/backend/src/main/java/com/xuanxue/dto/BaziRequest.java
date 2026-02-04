package com.xuanxue.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 八字计算请求
 */
@Data
public class BaziRequest {
    
    @NotNull(message = "出生年份不能为空")
    @Min(value = 1900, message = "出生年份不能早于1900年")
    @Max(value = 2100, message = "出生年份不能晚于2100年")
    private Integer year;
    
    @NotNull(message = "出生月份不能为空")
    @Min(value = 1, message = "月份范围为1-12")
    @Max(value = 12, message = "月份范围为1-12")
    private Integer month;
    
    @NotNull(message = "出生日期不能为空")
    @Min(value = 1, message = "日期范围为1-31")
    @Max(value = 31, message = "日期范围为1-31")
    private Integer day;
    
    @NotNull(message = "出生时辰不能为空")
    @Min(value = 0, message = "时辰范围为0-23")
    @Max(value = 23, message = "时辰范围为0-23")
    private Integer hour;
    
    /**
     * 性别: 1-男 2-女
     */
    private Integer gender;
    
    /**
     * 出生地点
     */
    private String birthPlace;
    
    /**
     * 是否为农历: false-公历 true-农历
     */
    private Boolean isLunar = false;
}
