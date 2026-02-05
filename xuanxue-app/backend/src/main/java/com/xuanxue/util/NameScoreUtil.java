package com.xuanxue.util;

import java.util.*;

/**
 * 姓名五格、五行等打分用工具（简易实现）
 */
public final class NameScoreUtil {

    private static final String[] WUXING = { "金", "木", "水", "火", "土" };

    // 常用起名用字（示例，可扩展）
    private static final String NAME_CHARS = "伟俊杰明浩宇浩然博文博涛子轩俊熙梓轩睿思嘉怡欣怡思涵诗涵雨涵" +
            "浩然宇轩宇航俊杰俊豪俊逸俊贤志远志强志明志伟志强思远思源思琪思涵" +
            "嘉怡嘉欣嘉慧嘉悦嘉宁嘉琪嘉懿欣怡欣然欣悦欣妍欣怡" +
            "雨涵雨欣雨晴雨桐雨萱雨薇雨婷雨晨" +
            "文博文昊文轩文涛文斌文杰文浩文昊" +
            "子涵子轩子墨子睿子豪子谦子健子恒";

    // 字到五行的简易映射（按笔画数取模 5 的简化）
    private static final Map<String, String> CHAR_WUXING = new HashMap<>();

    static {
        for (int i = 0; i < NAME_CHARS.length(); i++) {
            String c = String.valueOf(NAME_CHARS.charAt(i));
            CHAR_WUXING.put(c, WUXING[i % 5]);
        }
    }

    private NameScoreUtil() {}

    public static List<String> getNameCharList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < NAME_CHARS.length(); i++) {
            list.add(String.valueOf(NAME_CHARS.charAt(i)));
        }
        return list;
    }

    public static List<String> getWuxingFilteredChars(String wuxing) {
        if (wuxing == null || wuxing.isEmpty()) return getNameCharList();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> e : CHAR_WUXING.entrySet()) {
            if (wuxing.equals(e.getValue())) list.add(e.getKey());
        }
        return list.isEmpty() ? getNameCharList() : list;
    }

    public static String getWuxing(String singleChar) {
        return CHAR_WUXING.getOrDefault(singleChar, "土");
    }

    /**
     * 五格数理：天格、人格、地格、外格、总格（用笔画数近似，此处用字符编码简化）
     */
    public static int[] getFiveGe(String surname, String given) {
        int tian = strokeCount(surname);
        int ren = tian + (given.isEmpty() ? 0 : strokeCount(given.substring(0, 1)));
        int di = given.isEmpty() ? 0 : strokeCount(given);
        int wai = given.length() <= 1 ? 1 : (strokeCount(given.substring(given.length() - 1)) + 1);
        int zong = tian + strokeCount(given);
        return new int[] { tian, ren, di, wai, zong };
    }

    private static int strokeCount(String s) {
        if (s == null || s.isEmpty()) return 0;
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            sum += (c % 12) + 1;
        }
        return Math.max(1, sum);
    }

    /**
     * 五格综合得分 0-100
     */
    public static int scoreFiveGe(int[] ge) {
        if (ge == null || ge.length < 5) return 70;
        int score = 70;
        for (int g : ge) {
            if (g >= 1 && g <= 81) score += 2;
        }
        return Math.min(98, Math.max(50, score));
    }
}
