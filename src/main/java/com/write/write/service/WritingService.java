package com.write.write.service;

import com.write.write.dto.WritingRequest;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.WritingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WritingService {

    private final RestTemplate restTemplate;
    private final WritingRecordRepository recordRepository;

    @Value("${ai.apiUrl}")
    private String apiUrl;

    @Value("${ai.apiKey}")
    private String apiKey;

    public String handleRequest(WritingRequest req, UserAccount user) {
        String prompt = buildPrompt(req);
        String aiResponse = callAI(prompt);

        WritingRecord record = WritingRecord.builder()
                .user(user)
                .inputType(getInputType(req))
                .inputContent(prompt)
                .aiResponse(aiResponse)
                .build();
        recordRepository.save(record);

        return aiResponse;
    }

    public List<WritingRecord> getHistory(UserAccount user) {
        return recordRepository.findByUserOrderByCreatedAtDesc(user);
    }

    private String callAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = Map.of(
                "model", "deepseek-ai/DeepSeek-V2.5",
                "messages", List.of(
                        Map.of("role", "system", "content", "你是作文指导老师，擅长五感训练法"),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

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
