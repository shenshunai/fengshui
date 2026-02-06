package com.xuanxue.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 八字排盘核心算法
 */
public class BaziCalculator {
    
    // 天干
    private static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    
    // 地支
    private static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    
    // 天干五行
    private static final Map<String, String> GAN_ELEMENT = Map.of(
        "甲", "木", "乙", "木", "丙", "火", "丁", "火", "戊", "土",
        "己", "土", "庚", "金", "辛", "金", "壬", "水", "癸", "水"
    );
    
    // 地支五行
    private static final Map<String, String> ZHI_ELEMENT = Map.ofEntries(
        Map.entry("子", "水"), Map.entry("丑", "土"), Map.entry("寅", "木"),
        Map.entry("卯", "木"), Map.entry("辰", "土"), Map.entry("巳", "火"),
        Map.entry("午", "火"), Map.entry("未", "土"), Map.entry("申", "金"),
        Map.entry("酉", "金"), Map.entry("戌", "土"), Map.entry("亥", "水")
    );
    
    // 地支藏干
    private static final Map<String, List<String>> ZHI_HIDDEN_GAN = Map.ofEntries(
        Map.entry("子", List.of("癸")),
        Map.entry("丑", List.of("己", "癸", "辛")),
        Map.entry("寅", List.of("甲", "丙", "戊")),
        Map.entry("卯", List.of("乙")),
        Map.entry("辰", List.of("戊", "乙", "癸")),
        Map.entry("巳", List.of("丙", "庚", "戊")),
        Map.entry("午", List.of("丁", "己")),
        Map.entry("未", List.of("己", "丁", "乙")),
        Map.entry("申", List.of("庚", "壬", "戊")),
        Map.entry("酉", List.of("辛")),
        Map.entry("戌", List.of("戊", "辛", "丁")),
        Map.entry("亥", List.of("壬", "甲"))
    );
    
    // 基准日期: 1900年1月1日是甲戌日
    private static final LocalDate BASE_DATE = LocalDate.of(1900, 1, 1);
    private static final int BASE_GAN_ZHI_INDEX = 10; // 甲戌在60甲子中的索引
    
    /**
     * 八字结果
     */
    @Data
    @AllArgsConstructor
    public static class BaziResult {
        private GanZhi yearPillar;    // 年柱
        private GanZhi monthPillar;   // 月柱
        private GanZhi dayPillar;     // 日柱
        private GanZhi hourPillar;    // 时柱
        private Map<String, Integer> fiveElements;  // 五行统计
        private String dayMaster;     // 日主
        private String dayMasterStrength;  // 日主强弱
        private List<String> favorable;    // 喜用神
        private List<String> unfavorable;  // 忌神
    }
    
    /**
     * 干支
     */
    @Data
    @AllArgsConstructor
    public static class GanZhi {
        private String gan;  // 天干
        private String zhi;  // 地支
        
        @Override
        public String toString() {
            return gan + zhi;
        }
        
        public String getGanElement() {
            return GAN_ELEMENT.get(gan);
        }
        
        public String getZhiElement() {
            return ZHI_ELEMENT.get(zhi);
        }
        
        public List<String> getHiddenGan() {
            return ZHI_HIDDEN_GAN.get(zhi);
        }
    }
    
    /**
     * 计算八字
     * @param year 出生年（公历）
     * @param month 出生月（公历）
     * @param day 出生日（公历）
     * @param hour 出生时（0-23）
     * @return 八字结果
     */
    public static BaziResult calculate(int year, int month, int day, int hour) {
        // 计算四柱
        GanZhi yearPillar = calculateYearPillar(year);
        GanZhi monthPillar = calculateMonthPillar(year, month);
        GanZhi dayPillar = calculateDayPillar(year, month, day);
        GanZhi hourPillar = calculateHourPillar(dayPillar.getGan(), hour);
        
        // 计算五行
        Map<String, Integer> fiveElements = calculateFiveElements(
            yearPillar, monthPillar, dayPillar, hourPillar
        );
        
        // 日主
        String dayMaster = dayPillar.getGan();
        String dayMasterElement = GAN_ELEMENT.get(dayMaster);
        
        // 分析日主强弱
        String dayMasterStrength = analyzeDayMasterStrength(dayMasterElement, fiveElements);
        
        // 确定喜用神
        Map<String, List<String>> favorableMap = determineFavorableElements(dayMasterElement, dayMasterStrength);
        
        return new BaziResult(
            yearPillar, monthPillar, dayPillar, hourPillar,
            fiveElements, dayMaster, dayMasterStrength,
            favorableMap.get("favorable"), favorableMap.get("unfavorable")
        );
    }
    
    /**
     * 计算年柱
     */
    public static GanZhi calculateYearPillar(int year) {
        int ganIndex = (year - 4) % 10;
        if (ganIndex < 0) ganIndex += 10;
        int zhiIndex = (year - 4) % 12;
        if (zhiIndex < 0) zhiIndex += 12;
        return new GanZhi(TIAN_GAN[ganIndex], DI_ZHI[zhiIndex]);
    }
    
    /**
     * 计算月柱
     * 月支固定：正月寅、二月卯...
     * 月干根据年干推算（五虎遁）
     */
    public static GanZhi calculateMonthPillar(int year, int month) {
        // 月支索引（正月为寅，即索引2）
        int zhiIndex = (month + 1) % 12;
        
        // 年干索引
        int yearGanIndex = (year - 4) % 10;
        if (yearGanIndex < 0) yearGanIndex += 10;
        
        // 五虎遁：甲己之年丙作首，乙庚之年戊为头，丙辛之年寻庚上，丁壬壬寅顺行流，戊癸之年何处起，甲寅之上好追求
        int[] monthGanStart = {2, 4, 6, 8, 0}; // 丙、戊、庚、壬、甲
        int startGan = monthGanStart[yearGanIndex % 5];
        int ganIndex = (startGan + month - 1) % 10;
        
        return new GanZhi(TIAN_GAN[ganIndex], DI_ZHI[zhiIndex]);
    }
    
    /**
     * 计算日柱
     * 以1900年1月1日（甲戌日）为基准
     */
    public static GanZhi calculateDayPillar(int year, int month, int day) {
        LocalDate birthDate = LocalDate.of(year, month, day);
        long daysDiff = ChronoUnit.DAYS.between(BASE_DATE, birthDate);
        
        // 计算干支索引
        int ganZhiIndex = (int) ((BASE_GAN_ZHI_INDEX + daysDiff) % 60);
        if (ganZhiIndex < 0) ganZhiIndex += 60;
        int ganIndex = ganZhiIndex % 10;
        int zhiIndex = ganZhiIndex % 12;
        
        return new GanZhi(TIAN_GAN[ganIndex], DI_ZHI[zhiIndex]);
    }
    
    /**
     * 计算时柱
     * 时支根据小时确定
     * 时干根据日干推算（五鼠遁）
     */
    public static GanZhi calculateHourPillar(String dayGan, int hour) {
        // 确定时支索引
        int zhiIndex = hourToZhiIndex(hour);
        
        // 日干索引
        int dayGanIndex = Arrays.asList(TIAN_GAN).indexOf(dayGan);
        
        // 五鼠遁：甲己还生甲，乙庚丙作初，丙辛从戊起，丁壬庚子居，戊癸何方发，壬子是真途
        int[] hourGanStart = {0, 2, 4, 6, 8}; // 甲、丙、戊、庚、壬
        int startGan = hourGanStart[dayGanIndex % 5];
        int ganIndex = (startGan + zhiIndex) % 10;
        
        return new GanZhi(TIAN_GAN[ganIndex], DI_ZHI[zhiIndex]);
    }
    
    /**
     * 小时转地支索引
     */
    private static int hourToZhiIndex(int hour) {
        if (hour == 23 || hour == 0) return 0;  // 子时
        return (hour + 1) / 2;
    }
    
    /**
     * 计算五行统计
     */
    public static Map<String, Integer> calculateFiveElements(GanZhi... pillars) {
        Map<String, Integer> elements = new LinkedHashMap<>();
        elements.put("金", 0);
        elements.put("木", 0);
        elements.put("水", 0);
        elements.put("火", 0);
        elements.put("土", 0);
        
        for (GanZhi pillar : pillars) {
            // 统计天干五行
            String ganElement = GAN_ELEMENT.get(pillar.getGan());
            elements.merge(ganElement, 1, (a, b) -> (a != null ? a : 0) + (b != null ? b : 0));
            
            // 统计地支五行
            String zhiElement = ZHI_ELEMENT.get(pillar.getZhi());
            elements.merge(zhiElement, 1, (a, b) -> (a != null ? a : 0) + (b != null ? b : 0));
        }
        
        return elements;
    }
    
    /**
     * 分析日主强弱
     */
    private static String analyzeDayMasterStrength(String dayElement, Map<String, Integer> fiveElements) {
        // 同类：日主本身 + 生日主的五行
        Map<String, String> generating = Map.of(
            "金", "土", "木", "水", "水", "金", "火", "木", "土", "火"
        );
        
        int sameCount = fiveElements.get(dayElement);
        sameCount += fiveElements.get(generating.get(dayElement));
        
        if (sameCount >= 5) {
            return "身强";
        } else if (sameCount <= 2) {
            return "身弱";
        } else {
            return "中和";
        }
    }
    
    /**
     * 确定喜用神
     */
    private static Map<String, List<String>> determineFavorableElements(String dayElement, String strength) {
        // 五行相生相克关系
        Map<String, String> generating = Map.of(
            "金", "土", "木", "水", "水", "金", "火", "木", "土", "火"
        );
        Map<String, String> generated = Map.of(
            "金", "水", "木", "火", "水", "木", "火", "土", "土", "金"
        );
        Map<String, String> controlling = Map.of(
            "金", "火", "木", "金", "水", "土", "火", "水", "土", "木"
        );
        Map<String, String> controlled = Map.of(
            "金", "木", "木", "土", "水", "火", "火", "金", "土", "水"
        );
        
        List<String> favorable;
        List<String> unfavorable;
        
        if ("身强".equals(strength)) {
            // 身强喜克泄耗
            favorable = List.of(controlled.get(dayElement), generated.get(dayElement), controlling.get(dayElement));
            unfavorable = List.of(dayElement, generating.get(dayElement));
        } else {
            // 身弱喜生扶
            favorable = List.of(dayElement, generating.get(dayElement));
            unfavorable = List.of(controlled.get(dayElement), generated.get(dayElement), controlling.get(dayElement));
        }
        
        return Map.of("favorable", favorable, "unfavorable", unfavorable);
    }
    
    /**
     * 获取天干列表
     */
    public static String[] getTianGan() {
        return TIAN_GAN;
    }
    
    /**
     * 获取地支列表
     */
    public static String[] getDiZhi() {
        return DI_ZHI;
    }
    
    /**
     * 获取天干五行
     */
    public static String getGanElement(String gan) {
        return GAN_ELEMENT.get(gan);
    }
    
    /**
     * 获取地支五行
     */
    public static String getZhiElement(String zhi) {
        return ZHI_ELEMENT.get(zhi);
    }
}
