package com.platform.config;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网关统一错误响应格式（404、503 等返回 JSON）
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> defaultMap = super.getErrorAttributes(request, options);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", defaultMap.getOrDefault("status", 500));
        result.put("message", resolveMessage(defaultMap));
        result.put("path", defaultMap.get("path"));
        return result;
    }

    private String resolveMessage(Map<String, Object> defaultMap) {
        Object status = defaultMap.get("status");
        if (status instanceof Number) {
            int code = ((Number) status).intValue();
            if (code == 404) {
                return "路由不存在或服务未注册";
            }
            if (code == 503) {
                return "服务暂不可用，请稍后重试";
            }
            if (code == 504) {
                return "下游服务响应超时";
            }
        }
        return (String) defaultMap.getOrDefault("error", "网关异常");
    }
}
