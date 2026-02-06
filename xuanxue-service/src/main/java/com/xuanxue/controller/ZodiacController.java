package com.xuanxue.controller;

import com.platform.common.result.Result;
import com.xuanxue.dto.ZodiacFortuneResponse;
import com.xuanxue.service.ZodiacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 星座控制器
 */
@Tag(name = "星座接口", description = "星座运势、星座配对等")
@RestController
@RequestMapping("/api/zodiac")
@RequiredArgsConstructor
public class ZodiacController {
    
    private final ZodiacService zodiacService;
    
    @Operation(summary = "获取所有星座列表")
    @GetMapping("/list")
    public Result<List<String>> getAllZodiacSigns() {
        return Result.success(zodiacService.getAllZodiacSigns());
    }
    
    @Operation(summary = "根据生日获取星座")
    @GetMapping("/sign")
    public Result<String> getZodiacSign(
            @RequestParam Integer month,
            @RequestParam Integer day) {
        return Result.success(zodiacService.getZodiacSign(month, day));
    }
    
    @Operation(summary = "获取今日运势")
    @GetMapping("/fortune/today/{sign}")
    public Result<ZodiacFortuneResponse> getTodayFortune(@PathVariable String sign) {
        return Result.success(zodiacService.getTodayFortune(sign));
    }
    
    @Operation(summary = "获取指定日期运势")
    @GetMapping("/fortune/{sign}")
    public Result<ZodiacFortuneResponse> getFortune(
            @PathVariable String sign,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(zodiacService.getFortune(sign, date));
    }
    
    @Operation(summary = "获取本周运势")
    @GetMapping("/fortune/weekly/{sign}")
    public Result<ZodiacFortuneResponse> getWeeklyFortune(@PathVariable String sign) {
        return Result.success(zodiacService.getWeeklyFortune(sign));
    }
    
    @Operation(summary = "获取本月运势")
    @GetMapping("/fortune/monthly/{sign}")
    public Result<ZodiacFortuneResponse> getMonthlyFortune(@PathVariable String sign) {
        return Result.success(zodiacService.getMonthlyFortune(sign));
    }
    
    @Operation(summary = "星座配对")
    @GetMapping("/compatibility")
    public Result<String> getCompatibility(
            @RequestParam String sign1,
            @RequestParam String sign2) {
        return Result.success(zodiacService.getCompatibility(sign1, sign2));
    }
}
