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
            String topic = req.getTopic() != null ? req.getTopic() : "作文";
            String requirement = req.getRequirement() != null ? req.getRequirement() : "无特殊要求";
            
            return "请你作为一名专业的语文作文教师，对学生作文进行认真、客观的批改，并严格遵守以下规则。\n\n"
                    + "【评分标准】\n"
                    + "总分 100 分 = 内容（30 分）+ 结构（20 分）+ 语言（30 分）+ 创意（20 分）\n\n"
                    + "【核心规则（非常重要）】\n"
                    + "1. 必须包含【总体评价】，用于整体评价作文水平，供前端单独展示。\n"
                    + "2. \"内容 / 结构 / 语言 / 创意\"四个评分模块中：\n"
                    + "   - 只允许评价作文现状（优点和不足）\n"
                    + "   - 不得出现任何修改建议或指导性语言\n"
                    + "   - 禁止出现\"建议 / 可以 / 应当 / 如果……会更好\"等表达\n"
                    + "3. 所有修改建议只能出现在【修改建议】部分，不得出现在评分模块中。\n"
                    + "4. 严格按照下面的输出格式生成内容，标题、顺序、格式均不得改变。\n"
                    + "5. 各部分必须控制在指定字数范围内，语言简洁、具体。\n\n"
                    + "【输出格式要求（必须完全一致）】\n\n"
                    + "第一行必须是：\n"
                    + "评分：XX分（总分）\n\n"
                    + "**总体评价**\n"
                    + "从整体角度评价作文的完成情况、水平层次和主要特点，不重复分项点评内容。（80字以内）\n\n"
                    + "**内容评分：XX/30分**\n"
                    + "从主题立意、材料选择、内容充实程度等方面进行评价，不得出现修改建议。（100字以内）\n\n"
                    + "**结构评分：XX/20分**\n"
                    + "从文章结构、层次安排、段落衔接等方面进行评价，不得出现修改建议。（80字以内）\n\n"
                    + "**语言评分：XX/30分**\n"
                    + "从语言表达效果进行评价，不得出现修改建议。（100字以内）\n\n"
                    + "**创意评分：XX/20分**\n"
                    + "从立意角度、选材角度或表达方式的新颖性进行评价，不得出现修改建议。（80字以内）\n\n"
                    + "**修改建议**\n"
                    + "1. 针对内容、结构、语言或创意中存在的问题，给出具体、可操作的修改方法，并说明如何修改。\n"
                    + "2. 建议应结合作文实际，不得空泛，不得重复评分模块中的表述。\n"
                    + "3. 每条建议需包含明确方向和具体做法，帮助学生理解如何改进。\n\n"
                    + "【作文题目】" + topic + "\n"
                    + "【题目要求】" + requirement + "\n"
                    + "【作文内容】\n" + req.getEssay();
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

    /** 从AI响应中提取四个维度评分（内容/结构/语言/创意） */
    public Map<String, Integer> extractDimensionScores(String aiResponse) {
        Map<String, Integer> result = new HashMap<>();
        result.put("content", null);
        result.put("structure", null);
        result.put("language", null);
        result.put("creativity", null);

        if (aiResponse == null || aiResponse.isEmpty()) {
            return result;
        }

        try {
            // 支持两种标点形式与可选加粗：内容评分：XX/30分 或 **内容评分：XX/30分**
            String text = aiResponse.replace("**", "");

            Integer content = extractByPattern(text, "内容评分[：:](\\s*)?(\\d+)/(30)分");
            Integer structure = extractByPattern(text, "结构评分[：:](\\s*)?(\\d+)/(20)分");
            Integer language = extractByPattern(text, "语言评分[：:](\\s*)?(\\d+)/(30)分");
            Integer creativity = extractByPattern(text, "创意评分[：:](\\s*)?(\\d+)/(20)分");

            result.put("content", content);
            result.put("structure", structure);
            result.put("language", language);
            result.put("creativity", creativity);
        } catch (Exception e) {
            System.err.println("❌ 提取四维评分失败: " + e.getMessage());
        }
        return result;
    }

    private Integer extractByPattern(String text, String regex) {
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher m = p.matcher(text);
            if (m.find()) {
                String numStr = m.group(2);
                if (numStr != null) {
                    int v = Integer.parseInt(numStr);
                    return v;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
