package com.xuanxue.controller;

import com.xuanxue.client.AiServiceClient;
import com.platform.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 风水（优先 ChatGPT 生成内容）
 */
@Tag(name = "风水接口", description = "风水常识，由 AI 生成")
@RestController
@RequestMapping("/api/fengshui")
@RequiredArgsConstructor
public class FengshuiController {

    private final AiServiceClient aiServiceClient;

    @Operation(summary = "获取风水内容")
    @GetMapping("/content")
    public Result<Map<String, String>> getContent(@RequestParam(required = false) String category) {
        String content = aiServiceClient.getFengshuiContent(category);
        Map<String, String> data = new HashMap<>();
        data.put("content", content != null ? content : "");
        data.put("source", content != null ? "ai" : "static");
        return Result.success(data);
    }

    @Operation(summary = "获取 AI 服务状态（用于诊断调用失败原因）")
    @GetMapping("/status")
    public Result<Map<String, Object>> getStatus() {
        return Result.success(aiServiceClient.getAiServiceStatus());
    }
}
