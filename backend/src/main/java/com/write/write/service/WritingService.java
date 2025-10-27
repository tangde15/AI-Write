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

    /** 处理对比分析请求（重点：五感训练法）**/
    public String handleComparisonRequest(WritingRequest newReq, WritingRecord previousRecord) {
        String prompt = buildComparisonPrompt(newReq, previousRecord);
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

    /** 构建对比分析提示词（重点：五感训练法）**/
    private String buildComparisonPrompt(WritingRequest newReq, WritingRecord previousRecord) {
        return "你是一个专业的作文指导老师，擅长使用五感训练法提升学生的写作能力。\n\n" +
                "请对以下两篇作文进行对比分析，重点从五感训练法（视觉、听觉、味觉、嗅觉、触觉）的角度进行评价。\n\n" +
                "要求：\n" +
                "1. 对两篇作文分别进行评分（1-100分），第一行必须以「评分：XX分」开头\n" +
                "2. 重点评估每篇作文在五感描写方面的运用情况\n" +
                "3. 对比分析两篇作文在五感训练法应用上的进步\n" +
                "4. 给出具体的五感训练改进建议\n\n" +
                "评分标准：\n" +
                "- 五感描写（30分）：是否充分运用视觉、听觉、味觉、嗅觉、触觉\n" +
                "- 内容充实（30分）：情节完整，细节丰富\n" +
                "- 结构完整（20分）：开头、发展、结尾\n" +
                "- 语言表达（20分）：词汇丰富，句式多样\n\n" +
                "【历史作文】\n" +
                "题目：" + (previousRecord.getTopic() != null ? previousRecord.getTopic() : "") + "\n" +
                "内容：" + (previousRecord.getEssay() != null ? previousRecord.getEssay() : "") + "\n\n" +
                "【本次作文】\n" +
                "题目：" + (newReq.getTopic() != null ? newReq.getTopic() : "") + "\n" +
                "内容：" + (newReq.getEssay() != null ? newReq.getEssay() : "") + "\n\n" +
                "请按照以下格式输出：\n" +
                "【新作文评分】\n" +
                "评分：XX分\n" +
                "五感运用点评：[从五感描写角度点评]\n" +
                "整体评价：[综合评价]\n\n" +
                "【旧作文评分】\n" +
                "评分：XX分\n" +
                "五感运用点评：[从五感描写角度点评]\n\n" +
                "【对比分析 - 五感训练法进步情况】\n\n" +
                "✨ 五感描写方面的进步：\n" +
                "1. 视觉描写：旧作文如何 → 新作文如何\n" +
                "2. 听觉描写：旧作文如何 → 新作文如何\n" +
                "3. 味觉描写：旧作文如何 → 新作文如何\n" +
                "4. 嗅觉描写：旧作文如何 → 新作文如何\n" +
                "5. 触觉描写：旧作文如何 → 新作文如何\n\n" +
                "💪 仍需加强的五感：\n" +
                "1. [具体指出哪些感官描写还需要加强]\n" +
                "2. [给出具体的改进方法]\n\n" +
                "💡 五感训练建议：\n" +
                "1. [给出具体的五感训练方法]\n" +
                "2. [建议学生在哪些场景下可以运用五感]\n";
    }
}
