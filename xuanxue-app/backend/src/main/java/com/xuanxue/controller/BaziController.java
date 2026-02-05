package com.xuanxue.controller;

import com.xuanxue.dto.BaziRequest;
import com.xuanxue.dto.BaziResponse;
import com.xuanxue.dto.Result;
import com.xuanxue.service.BaziService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 八字控制器
 */
@Tag(name = "八字接口", description = "八字排盘、命理分析等")
@RestController
@RequestMapping("/api/bazi")
@RequiredArgsConstructor
public class BaziController {
    
    private final BaziService baziService;
    
    @Operation(summary = "计算八字")
    @PostMapping("/calculate")
    public Result<BaziResponse> calculate(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody BaziRequest request) {
        // 如果未登录，使用匿名用户ID
        if (userId == null) {
            userId = 0L;
        }
        BaziResponse response = baziService.calculate(userId, request);
        return Result.success(response);
    }
    
    @Operation(summary = "获取八字历史记录")
    @GetMapping("/history")
    public Result<List<BaziResponse>> getHistory(@AuthenticationPrincipal Long userId) {
        if (userId == null) {
            return Result.fail("请先登录");
        }
        List<BaziResponse> history = baziService.getHistory(userId);
        return Result.success(history);
    }
    
    @Operation(summary = "快速测试八字计算（无需登录）")
    @GetMapping("/test")
    public Result<BaziResponse> test(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day,
            @RequestParam Integer hour) {
        BaziRequest request = new BaziRequest();
        request.setYear(year);
        request.setMonth(month);
        request.setDay(day);
        request.setHour(hour);
        BaziResponse response = baziService.calculate(0L, request);
        return Result.success(response);
    }
}
