package com.xuanxue.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

/**
 * 星座运势响应
 */
@Data
@Builder
public class ZodiacFortuneResponse {
    
    /**
     * 星座名称
     */
    private String zodiacSign;
    
    /**
     * 元素属性
     */
    private String element;
    
    /**
     * 守护星
     */
    private String rulingPlanet;
    
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * 运势类型: daily/weekly/monthly/yearly
     */
    private String fortuneType;
    
    // ===== 各项评分 (1-100) =====
    
    private Integer overallScore;  // 综合运势
    private Integer loveScore;     // 爱情运势
    private Integer careerScore;   // 事业运势
    private Integer wealthScore;   // 财运
    private Integer healthScore;   // 健康运势
    
    // ===== 幸运元素 =====
    
    private String luckyColor;     // 幸运颜色
    private Integer luckyNumber;   // 幸运数字
    private String luckyDirection; // 幸运方位
    
    /**
     * 运势内容
     */
    private String content;
}
