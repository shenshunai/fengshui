package com.xuanxue.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;

/**
 * 调用 Python AI 服务（ChatGPT）
 */
@Slf4j
@Component
public class AiServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiServiceClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Value("${ai.service.enabled:false}")
    private boolean enabled;
    @Value("${ai.service.url:http://localhost:9000}")
    private String baseUrl;

    public boolean isEnabled() {
        return enabled && baseUrl != null && !baseUrl.isEmpty();
    }

    /**
     * 调用 AI 生成名字
     */
    public List<Map<String, Object>> generateNames(String surname, Integer gender,
                                                    Integer year, Integer month, Integer day,
                                                    String favorableElements, int count) {
        if (!isEnabled()) return null;
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("surname", surname != null ? surname : "王");
            body.put("gender", gender != null ? gender : 1);
            body.put("count", count);
            if (year != null) body.put("birth_year", year);
            if (month != null) body.put("birth_month", month);
            if (day != null) body.put("birth_day", day);
            if (favorableElements != null && !favorableElements.isEmpty()) {
                body.put("favorable_elements", favorableElements);
            }
            String json = objectMapper.writeValueAsString(body);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> resp = restTemplate.exchange(
                    baseUrl + "/api/ai/names",
                    HttpMethod.POST,
                    new HttpEntity<>(json, headers),
                    String.class
            );
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                JsonNode root = objectMapper.readTree(resp.getBody());
                JsonNode names = root.get("names");
                if (names != null && names.isArray()) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    for (JsonNode n : names) {
                        Map<String, Object> item = new HashMap<>();
                        if (n.has("name")) item.put("name", n.get("name").asText());
                        if (n.has("score")) item.put("score", n.get("score").asInt());
                        if (n.has("analysis")) item.put("analysis", n.get("analysis").asText());
                        list.add(item);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            log.warn("AI 起名服务调用失败，将使用本地规则: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 调用 AI 生成星座运势
     */
    public Map<String, Object> getZodiacFortune(String sign, String period) {
        if (!isEnabled()) return null;
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("sign", sign);
            body.put("period", period != null ? period : "today");
            String json = objectMapper.writeValueAsString(body);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> resp = restTemplate.exchange(
                    baseUrl + "/api/ai/zodiac",
                    HttpMethod.POST,
                    new HttpEntity<>(json, headers),
                    String.class
            );
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                return objectMapper.readValue(resp.getBody(), Map.class);
            }
        } catch (Exception e) {
            log.warn("AI 星座运势服务调用失败，将使用本地规则: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 调用 AI 生成风水内容
     */
    public String getFengshuiContent(String category) {
        if (!isEnabled()) return null;
        try {
            Map<String, Object> body = new HashMap<>();
            if (category != null && !category.isEmpty()) body.put("category", category);
            String json = objectMapper.writeValueAsString(body);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> resp = restTemplate.exchange(
                    baseUrl + "/api/ai/fengshui",
                    HttpMethod.POST,
                    new HttpEntity<>(json, headers),
                    String.class
            );
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                JsonNode root = objectMapper.readTree(resp.getBody());
                if (root.has("content")) return root.get("content").asText();
            }
        } catch (Exception e) {
            log.warn("AI 风水服务调用失败: {}", e.getMessage());
        }
        return null;
    }
}
