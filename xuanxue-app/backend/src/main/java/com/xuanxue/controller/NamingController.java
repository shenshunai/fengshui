package com.xuanxue.controller;

import com.xuanxue.dto.*;
import com.xuanxue.service.NamingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 起名与名字打分（优先 ChatGPT）
 */
@Tag(name = "起名接口", description = "新生儿起名、名字打分")
@RestController
@RequestMapping("/api/naming")
@RequiredArgsConstructor
public class NamingController {

    private final NamingService namingService;

    @Operation(summary = "根据八字等信息生成推荐名字")
    @PostMapping("/generate")
    public Result<List<NameItem>> generate(@Valid @RequestBody NamingRequest request) {
        List<NameItem> list = namingService.generateNames(request);
        return Result.success(list);
    }

    @Operation(summary = "对用户输入的名字进行评分")
    @PostMapping("/score")
    public Result<NameScoreResponse> score(@RequestBody NameScoreRequest request) {
        NameScoreResponse resp = namingService.scoreName(request);
        return Result.success(resp);
    }
}
