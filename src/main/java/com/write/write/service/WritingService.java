package com.write.write.service;

import com.write.write.dto.WritingRequest;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.WritingRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j // 自动生成日志对象 log
@Service
@RequiredArgsConstructor
public class WritingService {

    private final RestTemplate restTemplate;
    private final WritingRecordRepository repository;

    @Value("${ai.apiUrl}")
    private String apiUrl;

    @Value("${ai.apiKey}")
    private String apiKey;

    @Value("${ai.model}")
    private String model;


    public String handleRequest(WritingRequest req) {
        String prompt = buildPrompt(req);
        try {
            String aiResponse = callAI(prompt);

            // ✅ 使用 Lombok builder 构建实体对象
            WritingRecord record = WritingRecord.builder()
                    .inputType(getInputType(req))
                    .inputContent(prompt)
                    .aiResponse(aiResponse)
                    .build();

            repository.save(record);

            log.info("保存写作记录成功: type={}, id={}", record.getInputType(), record.getId());

            return aiResponse;
        } catch (Exception e) {
            log.error("AI 调用失败: {}", e.getMessage(), e);
            return "AI 服务暂时不可用，请稍后重试。（错误：" + e.getMessage() + "）";
        }
    }

    private String callAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "你是作文指导老师，擅长五感训练法"),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("AI 调用失败，HTTP状态：" + response.getStatusCode());
        }

        Map<String, Object> choice = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        return (String) message.get("content");
    }

    private String buildPrompt(WritingRequest req) {
        if (req.getEssay() != null && !req.getEssay().isEmpty())
            return "请点评以下作文并给出修改建议：" + req.getEssay();
        if (req.getTopic() != null && !req.getTopic().isEmpty())
            return "请给出作文提纲和五感写作提示，作文题目是：" + req.getTopic();
        if (req.getRequirement() != null && !req.getRequirement().isEmpty())
            return "请生成符合以下要求的写作思路和训练任务：" + req.getRequirement();
        return "用户没有输入任何内容";
    }

    private String getInputType(WritingRequest req) {
        if (req.getEssay() != null && !req.getEssay().isEmpty()) return "essay";
        if (req.getTopic() != null && !req.getTopic().isEmpty()) return "topic";
        if (req.getRequirement() != null && !req.getRequirement().isEmpty()) return "requirement";
        return "unknown";
    }
}
