package com.xuanxue.service;

import com.xuanxue.client.AiServiceClient;
import com.xuanxue.dto.*;
import com.xuanxue.util.BaziCalculator;
import com.xuanxue.util.LunarConverter;
import com.xuanxue.util.NameScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 起名与名字打分服务（优先调用 ChatGPT，失败则本地规则）
 */
@Service
@RequiredArgsConstructor
public class NamingService {

    private final AiServiceClient aiServiceClient;

    /**
     * 根据八字为新生儿起名（2-3字，含姓）
     */
    public List<NameItem> generateNames(NamingRequest req) {
        if (req.getSurname() == null || req.getSurname().trim().isEmpty()) {
            req.setSurname("王");
        }
        String surname = req.getSurname().trim().substring(0, 1);
        List<String> favorable = new ArrayList<>();
        if (req.getYear() != null && req.getMonth() != null && req.getDay() != null) {
            int y = req.getYear(), m = req.getMonth(), d = req.getDay();
            int h = req.getHour() != null ? req.getHour() : 12;
            if (Boolean.TRUE.equals(req.getIsLunar())) {
                int[] solar = LunarConverter.lunarToSolar(y, m, d);
                if (solar != null) {
                    y = solar[0]; m = solar[1]; d = solar[2];
                }
            }
            BaziCalculator.BaziResult bazi = BaziCalculator.calculate(y, m, d, h);
            favorable = bazi.getFavorable();
        }
        if (favorable.isEmpty()) {
            favorable = Arrays.asList("金", "木", "水", "火", "土");
        }
        String favorableStr = String.join(",", favorable);
        int count = req.getCount() != null && req.getCount() >= 1 && req.getCount() <= 20 ? req.getCount() : 6;

        List<Map<String, Object>> aiNames = aiServiceClient.generateNames(
                surname, req.getGender(), req.getYear(), req.getMonth(), req.getDay(),
                favorableStr, count);
        if (aiNames != null && !aiNames.isEmpty()) {
            List<NameItem> list = new ArrayList<>();
            for (Map<String, Object> m : aiNames) {
                String name = (String) m.get("name");
                if (name == null) continue;
                int score = m.get("score") instanceof Number ? ((Number) m.get("score")).intValue() : 85;
                String analysis = m.get("analysis") != null ? m.get("analysis").toString() : "寓意吉祥";
                list.add(NameItem.builder().name(name).score(score).analysis(analysis).build());
            }
            if (!list.isEmpty()) return list;
        }

        Set<String> used = new HashSet<>();
        List<NameItem> result = new ArrayList<>();
        List<String> pool = NameScoreUtil.getNameCharList();
        List<String> prefer = new ArrayList<>();
        for (String w : favorable) {
            prefer.addAll(NameScoreUtil.getWuxingFilteredChars(w));
        }
        if (prefer.isEmpty()) prefer = pool;
        Collections.shuffle(prefer);

        for (int tryCount = 0; result.size() < count && tryCount < 500; tryCount++) {
            String given;
            if (new Random().nextBoolean() && prefer.size() >= 2) {
                given = prefer.get(new Random().nextInt(prefer.size())) + prefer.get(new Random().nextInt(prefer.size()));
            } else if (!prefer.isEmpty()) {
                given = prefer.get(new Random().nextInt(prefer.size()));
            } else {
                given = pool.get(new Random().nextInt(pool.size()));
            }
            String fullName = surname + given;
            if (fullName.length() < 2 || fullName.length() > 3 || used.contains(fullName)) continue;
            used.add(fullName);
            int score = scoreNameInternal(surname, given, favorable);
            String analysis = "五格吉，读音响亮";
            if (!favorable.isEmpty()) analysis += "，契合八字喜用";
            result.add(NameItem.builder().name(fullName).score(score).analysis(analysis).build());
        }
        result.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return result.stream().limit(count).collect(Collectors.toList());
    }

    /**
     * 对用户输入的名字打分
     */
    public NameScoreResponse scoreName(NameScoreRequest req) {
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            return NameScoreResponse.builder()
                    .name("")
                    .score(0)
                    .summary("请输入姓名")
                    .details(Collections.emptyList())
                    .build();
        }
        String name = req.getName().trim().replace(" ", "");
        if (name.length() < 2 || name.length() > 4) {
            return NameScoreResponse.builder()
                    .name(name)
                    .score(0)
                    .summary("姓名长度建议2-4个字（含姓）")
                    .details(Collections.emptyList())
                    .build();
        }
        String surname = name.substring(0, 1);
        String given = name.substring(1);
        List<String> favorable = new ArrayList<>();
        if (req.getYear() != null && req.getMonth() != null && req.getDay() != null) {
            int y = req.getYear(), m = req.getMonth(), d = req.getDay();
            int h = req.getHour() != null ? req.getHour() : 12;
            if (Boolean.TRUE.equals(req.getIsLunar())) {
                int[] solar = LunarConverter.lunarToSolar(y, m, d);
                if (solar != null) { y = solar[0]; m = solar[1]; d = solar[2]; }
            }
            favorable = BaziCalculator.calculate(y, m, d, h).getFavorable();
        }
        int score = scoreNameInternal(surname, given, favorable);
        int[] ge = NameScoreUtil.getFiveGe(surname, given);
        List<String> details = new ArrayList<>();
        details.add("天格:" + ge[0] + " 人格:" + ge[1] + " 地格:" + ge[2] + " 外格:" + ge[3] + " 总格:" + ge[4]);
        details.add("五格数理" + (NameScoreUtil.scoreFiveGe(ge) >= 70 ? "吉" : "平"));
        if (!favorable.isEmpty()) {
            details.add("八字喜用: " + String.join("、", favorable));
        }
        String summary = score >= 85 ? "名字很好，寓意与数理俱佳。" : score >= 70 ? "名字不错，可考虑使用。" : "名字尚有提升空间，可参考五格与八字。";
        return NameScoreResponse.builder()
                .name(name)
                .score(score)
                .summary(summary)
                .details(details)
                .build();
    }

    private int scoreNameInternal(String surname, String given, List<String> favorable) {
        int[] ge = NameScoreUtil.getFiveGe(surname, given);
        int geScore = NameScoreUtil.scoreFiveGe(ge);
        int lenBonus = (surname.length() + given.length() >= 2 && surname.length() + given.length() <= 3) ? 5 : 0;
        int baziBonus = 0;
        if (!favorable.isEmpty() && given.length() >= 1) {
            for (int i = 0; i < given.length(); i++) {
                String w = NameScoreUtil.getWuxing(given.substring(i, i + 1));
                if (favorable.contains(w)) baziBonus += 3;
            }
        }
        return Math.min(98, Math.max(50, geScore + lenBonus + baziBonus));
    }
}
