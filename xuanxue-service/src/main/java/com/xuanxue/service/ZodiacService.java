package com.xuanxue.service;

import com.xuanxue.client.AiServiceClient;
import com.xuanxue.dto.ZodiacFortuneResponse;
import com.xuanxue.util.ZodiacCalculator;
import com.xuanxue.util.ZodiacCalculator.FortuneResult;
import com.xuanxue.util.ZodiacCalculator.ZodiacSign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 星座服务（优先 ChatGPT，失败则本地规则）
 */
@Service
@RequiredArgsConstructor
public class ZodiacService {

    private final AiServiceClient aiServiceClient;
    
    /**
     * 获取所有星座列表
     */
    public List<String> getAllZodiacSigns() {
        return Arrays.stream(ZodiacSign.values())
            .map(ZodiacSign::getChineseName)
            .collect(Collectors.toList());
    }
    
    /**
     * 根据生日获取星座
     */
    public String getZodiacSign(int month, int day) {
        return ZodiacCalculator.getZodiacSignName(month, day);
    }
    
    /**
     * 获取今日运势
     */
    public ZodiacFortuneResponse getTodayFortune(String signName) {
        Map<String, Object> ai = aiServiceClient.getZodiacFortune(signName, "today");
        if (ai != null && !ai.isEmpty()) {
            return mapToResponse(ai, LocalDate.now());
        }
        ZodiacSign sign = getZodiacSignByName(signName);
        if (sign == null) {
            throw new IllegalArgumentException("未知的星座: " + signName);
        }
        FortuneResult result = ZodiacCalculator.generateDailyFortune(sign, LocalDate.now());
        return toResponse(result);
    }
    
    /**
     * 获取指定日期运势
     */
    public ZodiacFortuneResponse getFortune(String signName, LocalDate date) {
        ZodiacSign sign = getZodiacSignByName(signName);
        if (sign == null) {
            throw new IllegalArgumentException("未知的星座: " + signName);
        }
        
        FortuneResult result = ZodiacCalculator.generateDailyFortune(sign, date);
        return toResponse(result);
    }
    
    /**
     * 获取本周运势
     */
    public ZodiacFortuneResponse getWeeklyFortune(String signName) {
        Map<String, Object> ai = aiServiceClient.getZodiacFortune(signName, "weekly");
        if (ai != null && !ai.isEmpty()) {
            ZodiacFortuneResponse r = mapToResponse(ai, LocalDate.now());
            r.setFortuneType("weekly");
            return r;
        }
        ZodiacSign sign = getZodiacSignByName(signName);
        if (sign == null) {
            throw new IllegalArgumentException("未知的星座: " + signName);
        }
        LocalDate monday = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        FortuneResult result = ZodiacCalculator.generateDailyFortune(sign, monday);
        ZodiacFortuneResponse response = toResponse(result);
        response.setFortuneType("weekly");
        response.setContent(generateWeeklyContent(sign, result));
        return response;
    }
    
    /**
     * 获取本月运势
     */
    public ZodiacFortuneResponse getMonthlyFortune(String signName) {
        Map<String, Object> ai = aiServiceClient.getZodiacFortune(signName, "monthly");
        if (ai != null && !ai.isEmpty()) {
            ZodiacFortuneResponse r = mapToResponse(ai, LocalDate.now());
            r.setFortuneType("monthly");
            return r;
        }
        ZodiacSign sign = getZodiacSignByName(signName);
        if (sign == null) {
            throw new IllegalArgumentException("未知的星座: " + signName);
        }
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        FortuneResult result = ZodiacCalculator.generateDailyFortune(sign, firstDay);
        ZodiacFortuneResponse response = toResponse(result);
        response.setFortuneType("monthly");
        response.setContent(generateMonthlyContent(sign, result));
        return response;
    }

    private ZodiacFortuneResponse mapToResponse(Map<String, Object> m, LocalDate date) {
        return ZodiacFortuneResponse.builder()
            .zodiacSign(getStr(m, "zodiacSign"))
            .element(getStr(m, "element"))
            .rulingPlanet(getStr(m, "rulingPlanet"))
            .date(date)
            .fortuneType(getStr(m, "fortuneType"))
            .overallScore(getInt(m, "overallScore", 80))
            .loveScore(getInt(m, "loveScore", 75))
            .careerScore(getInt(m, "careerScore", 78))
            .wealthScore(getInt(m, "wealthScore", 76))
            .healthScore(getInt(m, "healthScore", 82))
            .luckyColor(getStr(m, "luckyColor"))
            .luckyNumber(getInt(m, "luckyNumber", 7))
            .luckyDirection(getStr(m, "luckyDirection"))
            .content(getStr(m, "content"))
            .build();
    }

    private static String getStr(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : "";
    }

    private static int getInt(Map<String, Object> m, String key, int def) {
        Object v = m.get(key);
        if (v == null) return def;
        if (v instanceof Number) return ((Number) v).intValue();
        try {
            return Integer.parseInt(v.toString());
        } catch (Exception e) {
            return def;
        }
    }
    
    /**
     * 星座配对
     */
    public String getCompatibility(String sign1Name, String sign2Name) {
        ZodiacSign sign1 = getZodiacSignByName(sign1Name);
        ZodiacSign sign2 = getZodiacSignByName(sign2Name);
        
        if (sign1 == null || sign2 == null) {
            throw new IllegalArgumentException("未知的星座");
        }
        
        return ZodiacCalculator.getZodiacCompatibility(sign1, sign2);
    }
    
    /**
     * 根据名称获取星座枚举
     */
    private ZodiacSign getZodiacSignByName(String name) {
        for (ZodiacSign sign : ZodiacSign.values()) {
            if (sign.getChineseName().equals(name)) {
                return sign;
            }
        }
        return null;
    }
    
    /**
     * 转换为响应对象
     */
    private ZodiacFortuneResponse toResponse(FortuneResult result) {
        return ZodiacFortuneResponse.builder()
            .zodiacSign(result.getZodiacSign())
            .element(result.getElement())
            .rulingPlanet(result.getRulingPlanet())
            .date(result.getDate())
            .fortuneType("daily")
            .overallScore(result.getOverallScore())
            .loveScore(result.getLoveScore())
            .careerScore(result.getCareerScore())
            .wealthScore(result.getWealthScore())
            .healthScore(result.getHealthScore())
            .luckyColor(result.getLuckyColor())
            .luckyNumber(result.getLuckyNumber())
            .luckyDirection(result.getLuckyDirection())
            .content(result.getContent())
            .build();
    }
    
    /**
     * 生成周运势内容
     */
    private String generateWeeklyContent(ZodiacSign sign, FortuneResult result) {
        return String.format(
            "【%s本周运势】\n\n" +
            "◆ 综合运势：\n" +
            "本周整体运势%s，守护星%s的能量为你带来%s的影响。\n\n" +

            "◆ 事业运势：\n" +
            "工作方面会有新的机遇出现，适合推进重要项目。建议保持专注，\n" +
            "把握时机展现自己的能力。\n\n" +

            "◆ 感情运势：\n" +
            "感情生活较为平稳，单身者可以多参加社交活动，\n" +
            "有伴者注意多沟通交流。\n\n" +

            "◆ 财运分析：\n" +
            "财运方面需要谨慎理财，避免冲动消费。\n\n" +

            "◆ 健康提醒：\n" +
            "注意劳逸结合，保持规律作息。\n\n" +

            "◆ 幸运提示：\n" +
            "幸运颜色：%s\n" +
            "幸运数字：%d\n" +
            "幸运方位：%s\n",
            sign.getChineseName(),
            result.getOverallScore() >= 80 ? "极佳" : result.getOverallScore() >= 60 ? "不错" : "平稳",
            sign.getRulingPlanet(),
            "火".equals(sign.getElement()) ? "热情" : "水".equals(sign.getElement()) ? "灵动" : "稳定",
            result.getLuckyColor(),
            result.getLuckyNumber(),
            result.getLuckyDirection()
        );
    }
    
    /**
     * 生成月运势内容
     */
    private String generateMonthlyContent(ZodiacSign sign, FortuneResult result) {
        return String.format(
            "【%s本月运势】\n\n" +
            "◆ 本月概述：\n" +
            "本月对于%s来说是%s的一个月，%s象星座的特质会得到很好的发挥。\n\n" +
            "◆ 事业发展：\n" +
            "事业方面会有较大的进展空间，特别是月中时段，\n" +
            "会有重要的机会出现，要保持敏锐的洞察力。\n\n" +
            "◆ 感情生活：\n" +
            "感情方面桃花运%s，单身者可以主动出击，\n" +
            "有伴侣者感情会更加稳固。\n\n" +
            "◆ 财富机遇：\n" +
            "财运总体向好，可以考虑适当的投资理财，\n" +
            "但要注意风险控制。\n\n" +
            "◆ 健康状况：\n" +
            "身体状况良好，但要注意%s方面的保养。\n\n" +
            "◆ 本月幸运：\n" +
            "最佳日期：%d号、%d号\n" +
            "幸运颜色：%s\n" +
            "幸运数字：%d",
            sign.getChineseName(),
            sign.getChineseName(),
            result.getOverallScore() >= 80 ? "收获满满" : "稳步前进",
            sign.getElement(),
            result.getLoveScore() >= 80 ? "旺盛" : "平稳",
            sign.getElement().equals("火") ? "心脏" : "脾胃",
            result.getLuckyNumber(),
            result.getLuckyNumber() + 10,
            result.getLuckyColor(),
            result.getLuckyNumber()
        );
    }
}
