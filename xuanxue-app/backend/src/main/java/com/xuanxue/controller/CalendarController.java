package com.xuanxue.controller;

import com.xuanxue.client.AiServiceClient;
import com.xuanxue.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 万年历 - 今日宜忌（由 AI 生成）
 */
@Tag(name = "万年历接口", description = "今日宜忌，由 AI 生成")
@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final AiServiceClient aiServiceClient;

    @Operation(summary = "获取指定日期的今日宜忌")
    @GetMapping("/yiji")
    public Result<Map<String, Object>> getYiJi(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, Object> ai = aiServiceClient.getCalendarYiJi(dateStr);
        Map<String, Object> data = new HashMap<>();
        data.put("date", dateStr);
        if (ai != null) {
            data.put("yi", ai.get("yi"));
            data.put("ji", ai.get("ji"));
            data.put("summary", ai.get("summary"));
            data.put("source", "ai");
        } else {
            data.put("yi", "诸事不宜");
            data.put("ji", "无");
            data.put("summary", "暂无可用的宜忌数据，请确认 AI 服务已开启。");
            data.put("source", "fallback");
        }
        return Result.success(data);
    }
}
