package com.xuanxue.service;

import com.xuanxue.dto.BaziRequest;
import com.xuanxue.dto.BaziResponse;
import com.xuanxue.entity.BaziResult;
import com.xuanxue.repository.BaziResultRepository;
import com.xuanxue.util.BaziCalculator;
import com.xuanxue.util.BaziCalculator.GanZhi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 八字服务
 */
@Service
@RequiredArgsConstructor
public class BaziService {
    
    private final BaziResultRepository baziResultRepository;
    
    /**
     * 计算八字
     */
    @Transactional
    public BaziResponse calculate(Long userId, BaziRequest request) {
        // 计算八字
        BaziCalculator.BaziResult result = BaziCalculator.calculate(
            request.getYear(),
            request.getMonth(),
            request.getDay(),
            request.getHour()
        );
        
        // 保存结果
        BaziResult entity = new BaziResult();
        entity.setUserId(userId);
        entity.setBirthDatetime(LocalDateTime.of(
            request.getYear(), request.getMonth(), request.getDay(),
            request.getHour(), 0
        ));
        
        // 四柱
        entity.setYearGan(result.getYearPillar().getGan());
        entity.setYearZhi(result.getYearPillar().getZhi());
        entity.setMonthGan(result.getMonthPillar().getGan());
        entity.setMonthZhi(result.getMonthPillar().getZhi());
        entity.setDayGan(result.getDayPillar().getGan());
        entity.setDayZhi(result.getDayPillar().getZhi());
        entity.setHourGan(result.getHourPillar().getGan());
        entity.setHourZhi(result.getHourPillar().getZhi());
        
        // 五行
        Map<String, Integer> elements = result.getFiveElements();
        entity.setMetalCount(elements.get("金"));
        entity.setWoodCount(elements.get("木"));
        entity.setWaterCount(elements.get("水"));
        entity.setFireCount(elements.get("火"));
        entity.setEarthCount(elements.get("土"));
        
        // 命理分析
        entity.setDayMaster(result.getDayMaster());
        entity.setDayMasterStrength(result.getDayMasterStrength());
        entity.setFavorableElements(String.join(",", result.getFavorable()));
        entity.setUnfavorableElements(String.join(",", result.getUnfavorable()));
        
        // 生成AI分析
        entity.setAiAnalysis(generateAiAnalysis(result));
        
        baziResultRepository.save(entity);
        
        // 构建响应
        return buildResponse(result, entity);
    }
    
    /**
     * 获取用户八字历史
     */
    public List<BaziResponse> getHistory(Long userId) {
        return baziResultRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * 生成AI分析
     */
    private String generateAiAnalysis(BaziCalculator.BaziResult result) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("【八字命盘分析】\n\n");
        
        // 四柱展示
        sb.append("◆ 四柱八字：").append(result.getYearPillar()).append(" ")
          .append(result.getMonthPillar()).append(" ")
          .append(result.getDayPillar()).append(" ")
          .append(result.getHourPillar()).append("\n\n");
        
        // 日主分析
        sb.append("◆ 日主分析：\n");
        sb.append("  日主为「").append(result.getDayMaster()).append("」，五行属「")
          .append(BaziCalculator.getGanElement(result.getDayMaster())).append("」\n");
        sb.append("  日主强弱：").append(result.getDayMasterStrength()).append("\n\n");
        
        // 五行分布
        sb.append("◆ 五行分布：\n");
        Map<String, Integer> elements = result.getFiveElements();
        sb.append("  金：").append(elements.get("金")).append("个  ");
        sb.append("木：").append(elements.get("木")).append("个  ");
        sb.append("水：").append(elements.get("水")).append("个  ");
        sb.append("火：").append(elements.get("火")).append("个  ");
        sb.append("土：").append(elements.get("土")).append("个\n\n");
        
        // 喜用神
        sb.append("◆ 喜用神分析：\n");
        sb.append("  喜用：").append(String.join("、", result.getFavorable())).append("\n");
        sb.append("  忌神：").append(String.join("、", result.getUnfavorable())).append("\n\n");
        
        // 性格分析
        sb.append("◆ 性格特点：\n");
        sb.append(getPersonalityAnalysis(result.getDayMaster())).append("\n\n");
        
        // 事业建议
        sb.append("◆ 事业建议：\n");
        sb.append(getCareerAdvice(result.getFavorable())).append("\n");
        
        return sb.toString();
    }
    
    /**
     * 性格分析
     */
    private String getPersonalityAnalysis(String dayMaster) {
        if (dayMaster == null) {
            return "  性格特点分析中...";
        }
        switch (dayMaster) {
            case "甲": return "  甲木日主，如参天大树，性格正直刚强，有领导才能，但有时过于固执。";
            case "乙": return "  乙木日主，如花草藤萝，性格温和柔顺，善于适应环境，富有艺术气质。";
            case "丙": return "  丙火日主，如太阳之火，性格热情开朗，光明磊落，具有感染力和号召力。";
            case "丁": return "  丁火日主，如烛光星火，性格温和细腻，思维敏捷，善于观察和思考。";
            case "戊": return "  戊土日主，如大地山岳，性格稳重踏实，诚信可靠，具有包容心。";
            case "己": return "  己土日主，如田园沃土，性格温和谦逊，心思细腻，善于照顾他人。";
            case "庚": return "  庚金日主，如刀剑斧钺，性格刚毅果断，正义感强，做事雷厉风行。";
            case "辛": return "  辛金日主，如珠宝首饰，性格清高优雅，注重细节，有完美主义倾向。";
            case "壬": return "  壬水日主，如江河大海，性格豁达聪明，思维活跃，具有创新精神。";
            case "癸": return "  癸水日主，如雨露甘霖，性格温和内敛，直觉敏锐，富有同情心。";
            default: return "  性格特点分析中...";
        }
    }
    
    /**
     * 事业建议
     */
    private String getCareerAdvice(List<String> favorable) {
        StringBuilder sb = new StringBuilder();
        for (String element : favorable) {
            switch (element) {
                case "金":
                    sb.append("  适合从事金融、法律、机械、汽车等行业。\n");
                    break;
                case "木":
                    sb.append("  适合从事教育、文化、医药、环保等行业。\n");
                    break;
                case "水":
                    sb.append("  适合从事贸易、物流、旅游、传媒等行业。\n");
                    break;
                case "火":
                    sb.append("  适合从事餐饮、能源、科技、娱乐等行业。\n");
                    break;
                case "土":
                    sb.append("  适合从事房地产、建筑、农业、矿业等行业。\n");
                    break;
            }
        }
        return sb.toString();
    }
    
    /**
     * 实体转响应
     */
    private BaziResponse entityToResponse(BaziResult entity) {
        return BaziResponse.builder()
            .id(entity.getId())
            .birthDatetime(entity.getBirthDatetime())
            .yearPillar(entity.getYearGan() + entity.getYearZhi())
            .monthPillar(entity.getMonthGan() + entity.getMonthZhi())
            .dayPillar(entity.getDayGan() + entity.getDayZhi())
            .hourPillar(entity.getHourGan() + entity.getHourZhi())
            .metalCount(entity.getMetalCount())
            .woodCount(entity.getWoodCount())
            .waterCount(entity.getWaterCount())
            .fireCount(entity.getFireCount())
            .earthCount(entity.getEarthCount())
            .dayMaster(entity.getDayMaster())
            .dayMasterStrength(entity.getDayMasterStrength())
            .favorableElements(entity.getFavorableElements())
            .unfavorableElements(entity.getUnfavorableElements())
            .aiAnalysis(entity.getAiAnalysis())
            .createdAt(entity.getCreatedAt())
            .build();
    }
    
    /**
     * 构建响应
     */
    private BaziResponse buildResponse(BaziCalculator.BaziResult result, BaziResult entity) {
        return BaziResponse.builder()
            .id(entity.getId())
            .birthDatetime(entity.getBirthDatetime())
            .yearPillar(result.getYearPillar().toString())
            .monthPillar(result.getMonthPillar().toString())
            .dayPillar(result.getDayPillar().toString())
            .hourPillar(result.getHourPillar().toString())
            .metalCount(entity.getMetalCount())
            .woodCount(entity.getWoodCount())
            .waterCount(entity.getWaterCount())
            .fireCount(entity.getFireCount())
            .earthCount(entity.getEarthCount())
            .dayMaster(result.getDayMaster())
            .dayMasterStrength(result.getDayMasterStrength())
            .favorableElements(entity.getFavorableElements())
            .unfavorableElements(entity.getUnfavorableElements())
            .aiAnalysis(entity.getAiAnalysis())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
