package com.write.write.service;

import com.write.write.dto.WritingRequest;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.WritingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WritingService {

    private final WritingRecordRepository repository;
    
    // 配置超时时间为15分钟（900秒）
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(900))
            .setReadTimeout(Duration.ofSeconds(900))
            .build();

    @Value("${ai.api.url:https://api.siliconflow.cn/v1/chat/completions}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    /** 通用作文请求处理 **/
    public String handleRequest(WritingRequest req) {
        String prompt = buildPrompt(req);
        return callAI(prompt);
    }

    /** AI 调用封装 **/
    public String callAI(String prompt) {
        try {
            System.out.println("========== AI 调用开始 ==========");
            System.out.println("API URL: " + apiUrl);
            System.out.println("API Key: " + (apiKey != null ? "已配置" : "未配置"));
            System.out.println("提示词: " + prompt);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> payload = Map.of(
                    "model", "deepseek-ai/DeepSeek-V2.5",
                    "messages", List.of(
                            Map.of("role", "system", "content", "你是一个作文指导老师，擅长五感训练法"),
                            Map.of("role", "user", "content", prompt)
                    )
            );

            System.out.println("正在调用 AI 服务...");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            
            System.out.println("AI 响应状态码: " + response.getStatusCode());
            System.out.println("AI 响应体: " + response.getBody());

            Map<String, Object> choice = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            String content = (String) message.get("content");
            
            System.out.println("AI 返回内容: " + content);
            System.out.println("========== AI 调用成功 ==========");
            
            return content;
            
        } catch (Exception e) {
            System.err.println("========== AI 调用失败 ==========");
            System.err.println("错误类型: " + e.getClass().getName());
            System.err.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========== 错误详情结束 ==========");
            throw new RuntimeException("AI 调用失败: " + e.getMessage(), e);
        }
    }

    /** 根据输入构造提示 **/
    private String buildPrompt(WritingRequest req) {
        if (req.getEssay() != null && !req.getEssay().isEmpty()) {
            return "请作为专业的作文老师，对以下作文进行点评。\n\n"
                    + "要求：\n"
                    + "1. 第一行必须以「评分：XX分」开头（1-100分）\n"
                    + "2. 然后给出详细的点评和修改建议\n"
                    + "3. 评分标准：内容30分、结构20分、语言30分、创意20分\n\n"
                    + "作文内容：\n" + req.getEssay();
        }
        if (req.getTopic() != null && !req.getTopic().isEmpty())
            return "请根据作文题目生成写作提示与提纲：" + req.getTopic();
        if (req.getRequirement() != null && !req.getRequirement().isEmpty())
            return "请根据写作要求提供五感训练法建议：" + req.getRequirement();
        return "用户没有输入内容";
    }
    
    /** 从AI响应中提取评分 **/
    public Integer extractScore(String aiResponse) {
        if (aiResponse == null || aiResponse.isEmpty()) {
            return null;
        }
        
        try {
            // 尝试匹配 "评分：XX分" 或 "评分: XX分" 格式
            String[] lines = aiResponse.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("评分：") || line.startsWith("评分:")) {
                    // 提取数字
                    String scoreStr = line.replaceAll("[^0-9]", "");
                    if (!scoreStr.isEmpty()) {
                        int score = Integer.parseInt(scoreStr);
                        // 确保分数在1-100之间
                        if (score >= 1 && score <= 100) {
                            System.out.println("✅ 成功提取评分: " + score + "分");
                            return score;
                        }
                    }
                }
            }
            
            // 如果没找到评分，返回null
            System.out.println("⚠️ 未能从AI响应中提取评分");
            return null;
            
        } catch (Exception e) {
            System.err.println("❌ 提取评分失败: " + e.getMessage());
            return null;
        }
    }
}
