package com.xuanxue.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.*;

/**
 * 星座运势计算器
 */
public class ZodiacCalculator {
    
    /**
     * 星座枚举
     */
    public enum ZodiacSign {
        ARIES("白羊座", 3, 21, 4, 19, "火", "火星"),
        TAURUS("金牛座", 4, 20, 5, 20, "土", "金星"),
        GEMINI("双子座", 5, 21, 6, 21, "风", "水星"),
        CANCER("巨蟹座", 6, 22, 7, 22, "水", "月亮"),
        LEO("狮子座", 7, 23, 8, 22, "火", "太阳"),
        VIRGO("处女座", 8, 23, 9, 22, "土", "水星"),
        LIBRA("天秤座", 9, 23, 10, 23, "风", "金星"),
        SCORPIO("天蝎座", 10, 24, 11, 22, "水", "冥王星"),
        SAGITTARIUS("射手座", 11, 23, 12, 21, "火", "木星"),
        CAPRICORN("摩羯座", 12, 22, 1, 19, "土", "土星"),
        AQUARIUS("水瓶座", 1, 20, 2, 18, "风", "天王星"),
        PISCES("双鱼座", 2, 19, 3, 20, "水", "海王星");
        
        private final String chineseName;
        private final int startMonth;
        private final int startDay;
        private final int endMonth;
        private final int endDay;
        private final String element;
        private final String rulingPlanet;
        
        ZodiacSign(String chineseName, int startMonth, int startDay, 
                   int endMonth, int endDay, String element, String rulingPlanet) {
            this.chineseName = chineseName;
            this.startMonth = startMonth;
            this.startDay = startDay;
            this.endMonth = endMonth;
            this.endDay = endDay;
            this.element = element;
            this.rulingPlanet = rulingPlanet;
        }
        
        public String getChineseName() { return chineseName; }
        public String getElement() { return element; }
        public String getRulingPlanet() { return rulingPlanet; }
        
        public boolean isInRange(int month, int day) {
            if (this == CAPRICORN) {
                // 摩羯座跨年
                return (month == 12 && day >= startDay) || (month == 1 && day <= endDay);
            }
            if (month == startMonth && day >= startDay) return true;
            if (month == endMonth && day <= endDay) return true;
            return false;
        }
    }
    
    /**
     * 运势结果
     */
    @Data
    @AllArgsConstructor
    public static class FortuneResult {
        private String zodiacSign;      // 星座名称
        private String element;         // 元素属性
        private String rulingPlanet;    // 守护星
        private LocalDate date;         // 日期
        private int overallScore;       // 综合运势 (1-100)
        private int loveScore;          // 爱情运势
        private int careerScore;        // 事业运势
        private int wealthScore;        // 财运
        private int healthScore;        // 健康运势
        private String luckyColor;      // 幸运颜色
        private int luckyNumber;        // 幸运数字
        private String luckyDirection;  // 幸运方位
        private String content;         // 运势内容
    }
    
    // 幸运颜色列表
    private static final String[] LUCKY_COLORS = {
        "红色", "橙色", "黄色", "绿色", "蓝色", "紫色", "粉色", "白色", "黑色", "金色"
    };
    
    // 方位列表
    private static final String[] DIRECTIONS = {
        "东", "南", "西", "北", "东南", "东北", "西南", "西北"
    };
    
    /**
     * 根据出生日期获取星座
     * @param month 月份 (1-12)
     * @param day 日期
     * @return 星座
     */
    public static ZodiacSign getZodiacSign(int month, int day) {
        for (ZodiacSign sign : ZodiacSign.values()) {
            if (sign.isInRange(month, day)) {
                return sign;
            }
        }
        return null;
    }
    
    /**
     * 根据出生日期获取星座名称
     */
    public static String getZodiacSignName(int month, int day) {
        ZodiacSign sign = getZodiacSign(month, day);
        return sign != null ? sign.getChineseName() : "未知";
    }
    
    /**
     * 生成每日运势
     * @param zodiacSign 星座
     * @param date 日期
     * @return 运势结果
     */
    public static FortuneResult generateDailyFortune(ZodiacSign zodiacSign, LocalDate date) {
        // 使用日期作为随机种子，保证同一天运势一致
        long seed = date.toEpochDay() + zodiacSign.ordinal() * 1000;
        Random random = new Random(seed);
        
        // 生成各项评分 (60-100)
        int overallScore = 60 + random.nextInt(41);
        int loveScore = 60 + random.nextInt(41);
        int careerScore = 60 + random.nextInt(41);
        int wealthScore = 60 + random.nextInt(41);
        int healthScore = 60 + random.nextInt(41);
        
        // 幸运元素
        String luckyColor = LUCKY_COLORS[random.nextInt(LUCKY_COLORS.length)];
        int luckyNumber = 1 + random.nextInt(9);
        String luckyDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        
        // 生成运势内容
        String content = generateFortuneContent(zodiacSign, overallScore, loveScore, careerScore, random);
        
        return new FortuneResult(
            zodiacSign.getChineseName(),
            zodiacSign.getElement(),
            zodiacSign.getRulingPlanet(),
            date,
            overallScore,
            loveScore,
            careerScore,
            wealthScore,
            healthScore,
            luckyColor,
            luckyNumber,
            luckyDirection,
            content
        );
    }
    
    /**
     * 生成运势内容
     */
    private static String generateFortuneContent(ZodiacSign sign, int overall, int love, int career, Random random) {
        StringBuilder sb = new StringBuilder();
        
        // 综合运势
        sb.append("【综合运势】\n");
        if (overall >= 85) {
            sb.append("今日运势极佳！星象对你非常有利，是展现自我、大展宏图的好时机。\n\n");
        } else if (overall >= 70) {
            sb.append("今日运势不错，各方面都比较顺利，把握机会积极行动。\n\n");
        } else {
            sb.append("今日运势平稳，建议稳扎稳打，避免冒进。\n\n");
        }
        
        // 爱情运势
        sb.append("【爱情运势】\n");
        String[] loveTemplates = {
            "感情方面桃花运旺盛，单身者有望遇到心仪对象。",
            "与伴侣之间的感情稳定，适合一起进行浪漫活动。",
            "感情上需要多一些耐心和理解，避免因小事产生争执。"
        };
        sb.append(loveTemplates[random.nextInt(loveTemplates.length)]).append("\n\n");
        
        // 事业运势
        sb.append("【事业运势】\n");
        String[] careerTemplates = {
            "工作上会有新的机遇出现，贵人运强，适合拓展人脉。",
            "今日适合处理重要项目，你的创意和能力会得到认可。",
            "工作节奏可能会比较忙碌，注意合理安排时间。"
        };
        sb.append(careerTemplates[random.nextInt(careerTemplates.length)]).append("\n\n");
        
        // 温馨提示
        sb.append("【温馨提示】\n");
        String[] tips = {
            "保持积极乐观的心态，好运自然来。",
            "注意劳逸结合，身体是革命的本钱。",
            "多与朋友交流，会有意想不到的收获。"
        };
        sb.append(tips[random.nextInt(tips.length)]);
        
        return sb.toString();
    }
    
    /**
     * 获取星座配对分析
     */
    public static String getZodiacCompatibility(ZodiacSign sign1, ZodiacSign sign2) {
        // 简化的配对逻辑：同元素相配度高
        if (sign1.getElement().equals(sign2.getElement())) {
            return "非常契合！同属" + sign1.getElement() + "象星座，有很多共同话题和默契。";
        }
        
        // 火风相配、水土相配
        Set<String> compatible1 = Set.of("火", "风");
        Set<String> compatible2 = Set.of("水", "土");
        
        if ((compatible1.contains(sign1.getElement()) && compatible1.contains(sign2.getElement())) ||
            (compatible2.contains(sign1.getElement()) && compatible2.contains(sign2.getElement()))) {
            return "比较契合！" + sign1.getElement() + "象和" + sign2.getElement() + "象相互补充，能够和谐相处。";
        }
        
        return "需要磨合。" + sign1.getElement() + "象和" + sign2.getElement() + "象性格差异较大，需要更多理解和包容。";
    }
    
    /**
     * 获取生肖
     */
    public static String getChineseZodiac(int year) {
        String[] zodiacs = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        int index = (year - 4) % 12;
        if (index < 0) index += 12;
        return zodiacs[index];
    }
}
