package com.xuanxue.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 八字计算响应
 */
@Data
@Builder
public class BaziResponse {
    
    private Long id;
    
    /**
     * 出生日期时间
     */
    private LocalDateTime birthDatetime;
    
    // ===== 四柱 =====
    private String yearPillar;   // 年柱
    private String monthPillar;  // 月柱
    private String dayPillar;    // 日柱
    private String hourPillar;   // 时柱
    
    // ===== 五行统计 =====
    private Integer metalCount;  // 金
    private Integer woodCount;   // 木
    private Integer waterCount;  // 水
    private Integer fireCount;   // 火
    private Integer earthCount;  // 土
    
    // ===== 命理分析 =====
    private String dayMaster;           // 日主
    private String dayMasterStrength;   // 日主强弱
    private String favorableElements;   // 喜用五行
    private String unfavorableElements; // 忌神五行
    
    /**
     * AI分析结果
     */
    private String aiAnalysis;
    
    private LocalDateTime createdAt;
}
