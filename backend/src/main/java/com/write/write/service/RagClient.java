package com.write.write.service;

import com.write.write.config.RagConfig;
import com.write.write.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RAG 服务客户端
 * 调用 Python RAG 微服务（rag_service.py）
 */
@Slf4j
@Service
public class RagClient {

    private final RagConfig ragConfig;
    private final RestTemplate restTemplate;

    public RagClient(RagConfig ragConfig) {
        this.ragConfig = ragConfig;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(ragConfig.getTimeoutSeconds()))
                .setReadTimeout(Duration.ofSeconds(ragConfig.getTimeoutSeconds()))
                .build();
    }

    /**
     * 检查 RAG 服务是否可用
     */
    public boolean isAvailable() {
        if (!ragConfig.isEnabled()) {
            return false;
        }
        try {
            String healthUrl = ragConfig.getBaseUrl() + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("RAG 服务健康检查失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检索相似范文
     */
    public RagSearchResponse searchSimilarEssays(String essayText, int topK) {
        if (!ragConfig.isEnabled()) {
            log.info("RAG 服务未启用，跳过检索");
            return new RagSearchResponse(false, null, "RAG 服务未启用");
        }

        try {
            String url = ragConfig.getBaseUrl() + "/api/rag/search";
            RagSearchRequest request = new RagSearchRequest(essayText, topK);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RagSearchRequest> entity = new HttpEntity<>(request, headers);

            log.info("调用 RAG 检索服务: topK={}", topK);
            ResponseEntity<RagSearchResponse> response = restTemplate.postForEntity(
                    url, entity, RagSearchResponse.class
            );

            RagSearchResponse result = response.getBody();
            if (result != null && result.isSuccess()) {
                log.info("✅ RAG 检索成功，返回 {} 条结果", result.getResults().size());
            }
            return result;

        } catch (Exception e) {
            log.error("❌ RAG 检索失败: {}", e.getMessage());
            return new RagSearchResponse(false, null, "检索失败: " + e.getMessage());
        }
    }

    /**
     * 生成 RAG 批改建议
     */
    public RagFeedbackResponse generateFeedback(String essayText, String topic, String requirement) {
        if (!ragConfig.isEnabled()) {
            log.info("RAG 服务未启用，跳过生成");
            return new RagFeedbackResponse(false, null, "RAG 服务未启用");
        }

        try {
            String url = ragConfig.getBaseUrl() + "/api/rag/feedback";
            RagFeedbackRequest request = new RagFeedbackRequest(essayText, topic, requirement, null);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RagFeedbackRequest> entity = new HttpEntity<>(request, headers);

            log.info("调用 RAG 批改服务: 题目={}", topic);
            ResponseEntity<RagFeedbackResponse> response = restTemplate.postForEntity(
                    url, entity, RagFeedbackResponse.class
            );

            RagFeedbackResponse result = response.getBody();
            if (result != null && result.isSuccess()) {
                log.info("✅ RAG 批改生成成功，字数={}", result.getFeedback().length());
            }
            return result;

        } catch (Exception e) {
            log.error("❌ RAG 批改生成失败: {}", e.getMessage());
            return new RagFeedbackResponse(false, null, "批改生成失败: " + e.getMessage());
        }
    }
}
