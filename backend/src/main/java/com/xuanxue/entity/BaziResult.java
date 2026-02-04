package com.xuanxue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 八字结果实体
 */
@TableName("bazi_results")
@Data
public class BaziResult {
    
    @TableId
    private Long id;
    
    private Long userId;
    
    /**
     * 出生日期时间
     */
    private LocalDateTime birthDatetime;
    
    // ===== 四柱 =====
    
    /**
     * 年柱天干
     */
    private String yearGan;
    
    /**
     * 年柱地支
     */
    private String yearZhi;
    
    /**
     * 月柱天干
     */
    private String monthGan;
    
    /**
     * 月柱地支
     */
    private String monthZhi;
    
    /**
     * 日柱天干
     */
    private String dayGan;
    
    /**
     * 日柱地支
     */
    private String dayZhi;
    
    /**
     * 时柱天干
     */
    private String hourGan;
    
    /**
     * 时柱地支
     */
    private String hourZhi;
    
    // ===== 五行统计 =====
    
    private Integer metalCount = 0;  // 金
    private Integer woodCount = 0;   // 木
    private Integer waterCount = 0;  // 水
    private Integer fireCount = 0;   // 火
    private Integer earthCount = 0;  // 土
    
    // ===== 命理分析 =====
    
    /**
     * 日主
     */
    private String dayMaster;
    
    /**
     * 日主强弱: 身强/身弱/中和
     */
    private String dayMasterStrength;
    
    /**
     * 喜用五行
     */
    private String favorableElements;
    
    /**
     * 忌神五行
     */
    private String unfavorableElements;
    
    /**
     * AI分析结果
     */
    private String aiAnalysis;
    
    private LocalDateTime createdAt;

    /**
     * 获取完整四柱字符串
     */
    public String getFullPillars() {
        return String.format("%s%s %s%s %s%s %s%s", 
            yearGan, yearZhi, monthGan, monthZhi, 
            dayGan, dayZhi, hourGan, hourZhi);
    }
}
