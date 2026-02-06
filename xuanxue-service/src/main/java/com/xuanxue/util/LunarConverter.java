package com.xuanxue.util;

/**
 * 农历与公历转换（简易实现，仅作占位；生产环境建议使用专业库如 Lunar-Solar-Calendar-Converter）
 */
public final class LunarConverter {

    private LunarConverter() {}

    /**
     * 农历转公历（简易近似：若无法转换则返回 null，调用方将按公历处理）
     * @param lunarYear  农历年
     * @param lunarMonth 农历月 1-12
     * @param lunarDay   农历日
     * @return [公历年, 公历月, 公历日]，或 null
     */
    public static int[] lunarToSolar(int lunarYear, int lunarMonth, int lunarDay) {
        if (lunarYear < 1900 || lunarYear > 2100 || lunarMonth < 1 || lunarMonth > 12 || lunarDay < 1 || lunarDay > 30) {
            return null;
        }
        // 粗略近似：农历正月约对应公历 1 月下旬至 2 月，按 29.5 天/月估算
        int dayOfYear = (lunarMonth - 1) * 29 + lunarDay;
        int solarYear = lunarYear;
        if (dayOfYear <= 20) {
            solarYear = lunarYear - 1;
            dayOfYear += 365;
        }
        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (solarYear % 4 == 0 && (solarYear % 100 != 0 || solarYear % 400 == 0)) {
            daysInMonth[1] = 29;
        }
        dayOfYear -= 20; // 约正月初一在 1 月 20 日附近
        int solarMonth = 1;
        for (int i = 0; i < 12; i++) {
            if (dayOfYear <= daysInMonth[i]) {
                solarMonth = i + 1;
                break;
            }
            dayOfYear -= daysInMonth[i];
        }
        int solarDay = Math.min(dayOfYear, daysInMonth[solarMonth - 1]);
        if (solarDay < 1) solarDay = 1;
        return new int[] { solarYear, solarMonth, solarDay };
    }
}
