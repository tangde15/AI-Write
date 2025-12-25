package com.write.write.service;

import com.write.write.dto.WritingRequest;
import com.write.write.entity.*;
import com.write.write.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeService {
    private final PracticeLibraryRepository libraryRepo;
    private final PracticeSetRepository setRepo;
    private final PracticeBookRepository bookRepo;
    private final PracticeQuestionRepository questionRepo;
    private final PracticeAnswerRepository answerRepo;
    private final UserRepository userRepo;
    private final WritingService writingService;

    // ============== 系统批改与反馈解析 ==============
    public Map<String, Object> getSystemFeedback(Long questionId, Long userId) {
        PracticeAnswer ans = answerRepo.findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(questionId, userId)
                .orElse(null);
        
        if (ans == null || ans.getAiFeedback() == null || ans.getAiFeedback().isEmpty()) {
            return getDefaultSystemFeedback();
        }
        
        String feedback = ans.getAiFeedback();
        return parseAIFeedback(feedback, ans.getScore());
    }

    private Map<String, Object> getDefaultSystemFeedback() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalScore", 0);
        result.put("overallScore", 0.0);
        result.put("overallComment", "AI 正在努力生成详细批改～请稍候");
        result.put("improvements", "");
        result.put("contentScore", 0.0);
        result.put("contentComment", "");
        result.put("contentGuide", "");
        result.put("structureScore", 0.0);
        result.put("structureComment", "");
        result.put("structureGuide", "");
        result.put("languageScore", 0.0);
        result.put("languageComment", "");
        result.put("languageGuide", "");
        result.put("creativityScore", 0.0);
        result.put("creativityComment", "");
        result.put("creativityGuide", "");
        return result;
    }

    // ============== 列表数据（无数据则返回默认模拟数据） ==============
    public List<Map<String, Object>> listLibraries(int page, int size) {
        try {
            List<PracticeLibrary> libs = libraryRepo.findAll();
            if (libs.isEmpty()) {
                return getDefaultLibraries();
            }
            return libs.stream().map(l -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", l.getId());
                m.put("title", l.getTitle());
                m.put("description", l.getDescription());
                m.put("author", l.getAuthor());
                m.put("totalCount", Optional.ofNullable(l.getTotalCount()).orElse(0));
                m.put("createdAt", Optional.ofNullable(l.getCreatedAt()).orElse(LocalDateTime.now()).toString());
                return m;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            return getDefaultLibraries();
        }
    }

    public List<Map<String, Object>> listSets(int page, int size) {
        try {
            List<PracticeSet> sets = setRepo.findAll();
            if (sets.isEmpty()) return getDefaultSets();
            return sets.stream().map(s -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", s.getId());
                m.put("name", s.getName());
                m.put("createTime", Optional.ofNullable(s.getCreatedAt()).orElse(LocalDateTime.now()).toString());
                String creator = "系统";
                if (s.getCreatorId() != null) {
                    creator = userRepo.findById(s.getCreatorId())
                            .map(UserAccount::getUsername)
                            .orElse("系统");
                }
                m.put("creator", creator);
                return m;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            return getDefaultSets();
        }
    }

    // ============== 题库/题单下的练习册列表 ==============
    public Map<String, Object> getLibraryBooks(Long libraryId, Long userId) {
        PracticeLibrary library = libraryRepo.findById(libraryId).orElse(null);
        List<PracticeBook> books = bookRepo.findByLibraryIdOrderByIdAsc(libraryId);
        
        List<Map<String, Object>> bookList = books.stream().map(book -> {
            List<PracticeQuestion> questions = questionRepo.findByBookIdOrderByIdAsc(book.getId());
            long completed = 0;
            if (!questions.isEmpty()) {
                completed = answerRepo.countByQuestionIdInAndUserIdAndStatus(
                        questions.stream().map(PracticeQuestion::getId).collect(Collectors.toList()),
                        userId, "COMPLETED");
            }
            
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", book.getId());
            m.put("name", book.getName());
            m.put("questionCount", questions.size());
            m.put("completedCount", completed);
            m.put("creator", "系统");
            m.put("createdAt", book.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("libraryTitle", library != null ? library.getTitle() : "题库");
        result.put("books", bookList);
        return result;
    }

    public Map<String, Object> getSetBooks(Long setId, Long userId) {
        PracticeSet set = setRepo.findById(setId).orElse(null);
        List<PracticeBook> books = bookRepo.findBySetIdOrderByIdAsc(setId);
        
        List<Map<String, Object>> bookList = books.stream().map(book -> {
            List<PracticeQuestion> questions = questionRepo.findByBookIdOrderByIdAsc(book.getId());
            long completed = 0;
            if (!questions.isEmpty()) {
                completed = answerRepo.countByQuestionIdInAndUserIdAndStatus(
                        questions.stream().map(PracticeQuestion::getId).collect(Collectors.toList()),
                        userId, "COMPLETED");
            }
            
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", book.getId());
            m.put("name", book.getName());
            m.put("questionCount", questions.size());
            m.put("completedCount", completed);
            m.put("creator", "系统");
            m.put("createdAt", book.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("setName", set != null ? set.getName() : "题单");
        result.put("books", bookList);
        return result;
    }

    // ============== 练习册详情与题目列表 ==============
    public Map<String, Object> getBookDetail(Long bookId, Long userId) {
        PracticeBook book = bookRepo.findById(bookId).orElseGet(this::getDefaultBook);
        List<PracticeQuestion> questions = questionRepo.findByBookIdOrderByIdAsc(bookId);
        long completed = 0;
        if (!questions.isEmpty()) {
            completed = answerRepo.countByQuestionIdInAndUserIdAndStatus(
                    questions.stream().map(PracticeQuestion::getId).collect(Collectors.toList()),
                    userId, "COMPLETED");
        }
        return Map.of(
                "id", bookId,
                "name", book.getName(),
                "completedCount", completed,
                "totalCount", questions.isEmpty() ? 8 : questions.size()
        );
    }

    public List<Map<String, Object>> getBookQuestions(Long bookId, Long userId) {
        List<PracticeQuestion> qs = questionRepo.findByBookIdOrderByIdAsc(bookId);
        if (qs.isEmpty()) {
            System.out.println("⚠️ 练习册 " + bookId + " 没有题目，请先初始化数据");
            return new ArrayList<>();
        }

        Map<Long, PracticeAnswer> answerMap = answerRepo
                .findByQuestionIdInAndUserId(
                        qs.stream().map(PracticeQuestion::getId).collect(Collectors.toList()),
                        userId)
                .stream().collect(Collectors.toMap(PracticeAnswer::getQuestionId, a -> a, (a, b) -> a));

        List<Map<String, Object>> list = new ArrayList<>();
        for (PracticeQuestion q : qs) {
            PracticeAnswer ans = answerMap.get(q.getId());
            String completion = (ans != null && "COMPLETED".equals(ans.getStatus())) ? "已完成" : "未完成";
            String sys = (ans != null && ans.getAiFeedback() != null) ? "已批改" : "未批改";
            String teacher = (ans != null && ans.getTeacherFeedback() != null) ? "已批改" : "未批改";
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", q.getId());
            m.put("title", q.getTitle());
            m.put("completionStatus", completion);
            m.put("systemGrade", sys);
            m.put("teacherGrade", teacher);
            m.put("score", ans != null ? ans.getScore() : null);
            list.add(m);
        }
        return list;
    }

    public List<Map<String, Object>> getBookTimeline(Long bookId, Long userId) {
        List<PracticeQuestion> qs = questionRepo.findByBookIdOrderByIdAsc(bookId);
        if (qs.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> points = new ArrayList<>();
        int idx = 0;
        for (PracticeQuestion q : qs) {
            Map<String, Object> p = new LinkedHashMap<>();
            p.put("x", 10 + idx * 15);
            p.put("y", Math.min(90, 20 + idx * 10));
            p.put("label", "题目" + (idx + 1));
            points.add(p);
            idx++;
        }
        return points;
    }

    // ============== 题目详情与提交保存 ==============
    public Map<String, Object> getQuestionDetail(Long questionId, Long userId) {
        PracticeQuestion q = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + questionId));
        PracticeAnswer ans = answerRepo.findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(questionId, userId).orElse(null);
        Map<String, Object> info = new HashMap<>();
        info.put("id", q.getId());
        info.put("title", q.getTitle());  // ⭐ 关键：返回题目标题
        info.put("requirement", q.getRequirement());
        info.put("maxScore", q.getMaxScore());
        info.put("type", q.getType());
        info.put("creator", "用户名称");
        info.put("createTime", Optional.ofNullable(q.getCreatedAt()).orElse(LocalDateTime.now()).toString());
        info.put("score", ans != null ? ans.getScore() : q.getMaxScore());
        info.put("isFavorite", false);
        info.put("favoriteCount", 233);
        info.put("isCompleted", ans != null && "COMPLETED".equals(ans.getStatus()));
        info.put("hasSystemFeedback", ans != null && ans.getAiFeedback() != null);
        info.put("hasTeacherFeedback", ans != null && ans.getTeacherFeedback() != null);
        // 返回当前用户该题目的最新答案ID，供前端后续查询详情
        info.put("answerId", ans != null ? ans.getId() : null);
        info.put("answer", ans != null ? ans.getContent() : "");
        return info;
    }

    @Transactional
    public void saveDraft(Long questionId, Long userId, String content) {
        PracticeAnswer ans = answerRepo.findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(questionId, userId)
                .orElseGet(() -> {
                    PracticeAnswer a = new PracticeAnswer();
                    a.setQuestionId(questionId);
                    a.setUserId(userId);
                    a.setCreatedAt(LocalDateTime.now());
                    return a;
                });
        ans.setContent(content);
        ans.setStatus("INCOMPLETE");
        ans.setUpdatedAt(LocalDateTime.now());
        answerRepo.save(ans);
    }

    @Transactional
    public Map<String, Object> submit(Long questionId, Long userId, String content) {
        System.out.println("📥 收到提交请求");
        System.out.println("题目 ID: " + questionId);
        System.out.println("用户 ID: " + userId);
        System.out.println("内容长度: " + (content != null ? content.length() : 0) + " 字");
        
        // 检查是否有进行中的批改（防止重复提交相同内容）
        PracticeAnswer latest = answerRepo
                .findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(questionId, userId)
                .orElse(null);
        
        if (latest != null && "PROCESSING".equalsIgnoreCase(latest.getStatus())) {
            System.out.println("⚠️ 检测到进行中的批改，answerId: " + latest.getId());
            Map<String, Object> processing = new LinkedHashMap<>();
            processing.put("answerId", latest.getId());
            processing.put("status", "PROCESSING");
            return processing;
        }
        
        // 如果已有完成记录且内容相同，返回现有结果（避免重复批改相同内容）
        if (latest != null && "COMPLETED".equalsIgnoreCase(latest.getStatus()) 
                && content != null && content.equals(latest.getContent())) {
            System.out.println("ℹ️ 内容未变化，返回现有批改结果，answerId: " + latest.getId());
            Map<String, Object> existed = new LinkedHashMap<>();
            existed.put("answerId", latest.getId());
            existed.put("status", "DONE");
            existed.put("score", Optional.ofNullable(latest.getScore()).orElse(0));
            existed.put("aiFeedback", latest.getAiFeedback());
            return existed;
        }

        // 创建新的答案记录（允许重新作答）
        System.out.println("🆕 创建新答案记录");
        PracticeQuestion q = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + questionId));
        PracticeAnswer ans = new PracticeAnswer();
        ans.setQuestionId(questionId);
        ans.setUserId(userId);
        ans.setContent(content);
        ans.setStatus("PROCESSING");  // ⭐ 立即标记为处理中
        ans.setCreatedAt(LocalDateTime.now());
        ans.setUpdatedAt(LocalDateTime.now());
        ans = answerRepo.save(ans);
        
        System.out.println("✅ 答案已保存，ID: " + ans.getId() + "，状态: PROCESSING");
        System.out.println("🔄 正在异步启动 AI 批改...");
        
        // ⭐ 异步处理 AI 批改，不阻塞返回
        processAnswerAsync(ans.getId(), questionId, q);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("answerId", ans.getId());
        result.put("status", "PROCESSING");
        return result;
    }

    // ⭐ 异步处理 AI 批改（后台执行，不阻塞 HTTP 返回）
    @Async
    public void processAnswerAsync(Long answerId, Long questionId, PracticeQuestion q) {
        try {
            System.out.println("========== 异步批改开始 ==========");
            System.out.println("答案 ID: " + answerId);
            System.out.println("题目 ID: " + questionId);
            
            PracticeAnswer ans = answerRepo.findById(answerId).orElse(null);
            if (ans == null) {
                System.err.println("❌ 答案记录不存在");
                return;
            }

            // 批改逻辑
            Integer score = 0;
            String feedback = null;
            
            if ("OBJECTIVE".equalsIgnoreCase(q.getType())) {
                score = Objects.equals(ans.getContent() == null ? "" : ans.getContent().trim(),
                        Optional.ofNullable(q.getCorrectAnswer()).orElse("").trim()) ? q.getMaxScore() : 0;
                feedback = score != null && score > 0 ? "回答正确" : "回答不正确";
                System.out.println("✅ 客观题批改完成，分数: " + score);
            } else {
                try {
                    System.out.println("📝 准备调用 AI 批改主观题");
                    System.out.println("题目标题: " + q.getTitle());
                    System.out.println("题目要求: " + q.getRequirement());
                    System.out.println("作文内容长度: " + (ans.getContent() != null ? ans.getContent().length() : 0) + " 字");
                    
                    WritingRequest req = new WritingRequest();
                    req.setTopic(q.getTitle());
                    req.setEssay(ans.getContent());
                    req.setRequirement(q.getRequirement());
                    
                    System.out.println("📡 正在调用 AI 服务...");
                    long startTime = System.currentTimeMillis();
                    String ai = writingService.handleRequest(req);
                    long endTime = System.currentTimeMillis();
                    
                    System.out.println("✅ AI 调用成功，耗时: " + (endTime - startTime) + "ms");
                    System.out.println("AI 返回内容预览: " + (ai.length() > 200 ? ai.substring(0, 200) + "..." : ai));
                    
                    feedback = ai;
                    Integer extracted = writingService.extractScore(ai);
                    score = extracted != null ? extracted : Optional.ofNullable(q.getMaxScore()).orElse(100);
                    System.out.println("✅ AI 批改完成，提取分数: " + score);
                } catch (Exception ex) {
                    System.err.println("❌ AI 调用失败");
                    System.err.println("异常类型: " + ex.getClass().getName());
                    System.err.println("异常信息: " + ex.getMessage());
                    ex.printStackTrace();
                    
                    // AI 不可用时的回退
                    feedback = "系统批改服务暂不可用，已为你保存答案。建议稍后重试获得详细批改。\n\n错误信息: " + ex.getMessage();
                    score = Optional.ofNullable(q.getMaxScore()).orElse(100) - 20;
                    System.out.println("⚠️ 使用降级分数: " + score);
                }
            }
            
            ans.setScore(score);
            ans.setAiFeedback(feedback);
            ans.setStatus("COMPLETED");  // ⭐ 标记为已完成
            ans.setUpdatedAt(LocalDateTime.now());
            answerRepo.save(ans);
            
            System.out.println("✅ 批改结果已保存");
            System.out.println("========== 异步批改完成 ==========");
        } catch (Exception e) {
            System.err.println("❌ 异步批改异常: " + e.getMessage());
            e.printStackTrace();
            
            // 标记失败
            PracticeAnswer ans = answerRepo.findById(answerId).orElse(null);
            if (ans != null) {
                ans.setStatus("FAILED");
                ans.setAiFeedback("批改异常，请重试");
                answerRepo.save(ans);
            }
        }
    }

    // ⭐ 查询答案状态（前端轮询用）
    public Map<String, Object> getAnswerStatus(Long answerId) {
        PracticeAnswer ans = answerRepo.findById(answerId).orElse(null);
        Map<String, Object> result = new LinkedHashMap<>();
        
        if (ans == null) {
            result.put("status", "NOT_FOUND");
            return result;
        }
        
        result.put("answerId", ans.getId());
        result.put("status", ans.getStatus());
        
        if ("COMPLETED".equalsIgnoreCase(ans.getStatus())) {
            result.put("score", Optional.ofNullable(ans.getScore()).orElse(0));
            result.put("aiFeedback", Optional.ofNullable(ans.getAiFeedback()).orElse(""));
        }
        
        if ("FAILED".equalsIgnoreCase(ans.getStatus())) {
            result.put("errorMessage", Optional.ofNullable(ans.getAiFeedback()).orElse("未知错误"));
        }
        
        return result;
    }
    
    // ⭐ 获取系统批改详细反馈（通过题目ID查询最新答案）
    public Map<String, Object> getSystemFeedbackByQuestion(Long questionId, Long userId) {
        System.out.println("📊 开始获取系统反馈，questionId: " + questionId + ", userId: " + userId);
        
        PracticeAnswer ans = answerRepo
                .findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(questionId, userId)
                .orElse(null);
        
        if (ans == null) {
            System.out.println("❌ 未找到答案记录");
            return Map.of("error", "未找到答案记录");
        }
        
        System.out.println("✅ 找到答案记录，answerId: " + ans.getId());
        return getSystemFeedback(ans.getId());
    }
    
    // ⭐ 获取系统批改详细反馈（解析 AI 反馈为结构化数据）
    public Map<String, Object> getSystemFeedback(Long answerId) {
        System.out.println("📊 开始获取系统反馈，answerId: " + answerId);
        
        PracticeAnswer ans = answerRepo.findById(answerId).orElse(null);
        if (ans == null) {
            System.out.println("❌ 答案不存在");
            return Map.of("error", "答案不存在");
        }
        
        String aiFeedback = ans.getAiFeedback();
        Integer totalScore = ans.getScore();
        
        if (aiFeedback == null || aiFeedback.isEmpty()) {
            System.out.println("⚠️ AI 反馈为空");
            return Map.of("totalScore", totalScore != null ? totalScore : 0);
        }
        
        System.out.println("🔍 开始解析 AI 反馈，总分: " + totalScore);
        System.out.println("========== AI 反馈原文 START ==========");
        System.out.println(aiFeedback);
        System.out.println("========== AI 反馈原文 END ==========");
        
        Map<String, Object> result = parseAIFeedback(aiFeedback, totalScore);
        
        System.out.println("✅ 系统反馈解析完成");
        System.out.println("内容评分: " + result.get("contentScore"));
        System.out.println("结构评分: " + result.get("structureScore"));
        System.out.println("语言评分: " + result.get("languageScore"));
        
        return result;
    }
    
    // 解析 AI 反馈为结构化数据
    private Map<String, Object> parseAIFeedback(String feedback, Integer totalScore) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        // 总分
        result.put("totalScore", totalScore != null ? totalScore : 0);
        
        // ⭐ 提取总体评价（在总分之后、分项评分之前）
        // 注意：startPattern 使用非捕获组 (?:...) 而不是捕获组 (...)
        String overallComment = extractSection(feedback, "(?:总体评价)[：:：\\*]*", "(?:内容评分|结构评分|语言评分|创意评分)");
        overallComment = cleanExtractedText(overallComment);  // 清理括号说明
        result.put("overallComment", overallComment);
        
        // 提取各维度评分和评语
        Map<String, Object> content = extractDimension(feedback, "内容", "内容评分[：:：]?\\s*(\\d+)/(\\d+)分?");
        Map<String, Object> structure = extractDimension(feedback, "结构", "结构评分[：:：]?\\s*(\\d+)/(\\d+)分?");
        Map<String, Object> language = extractDimension(feedback, "语言", "语言评分[：:：]?\\s*(\\d+)/(\\d+)分?");
        Map<String, Object> creativity = extractDimension(feedback, "创意", "创意评分[：:：]?\\s*(\\d+)/(\\d+)分?");
        
        result.put("contentScore", content.get("ratio"));
        result.put("contentComment", content.get("comment"));
        result.put("contentGuide", content.get("guide"));
        
        result.put("structureScore", structure.get("ratio"));
        result.put("structureComment", structure.get("comment"));
        result.put("structureGuide", structure.get("guide"));
        
        result.put("languageScore", language.get("ratio"));
        result.put("languageComment", language.get("comment"));
        result.put("languageGuide", language.get("guide"));
        
        result.put("creativityScore", creativity.get("ratio"));
        result.put("creativityComment", creativity.get("comment"));
        result.put("creativityGuide", creativity.get("guide"));
        
        // 提取改进建议（支持"改进建议"、"修改建议"等多种标题）
        String improvements = extractSection(feedback, "(?:改进建议|修改建议)[：:：\\*]*", null);
        improvements = cleanExtractedText(improvements);  // 清理格式
        result.put("improvements", improvements);
        
        System.out.println("📝 解析结果:");
        System.out.println("  总体评价: " + (overallComment.isEmpty() ? "无" : overallComment.substring(0, Math.min(50, overallComment.length())) + "..."));
        System.out.println("  改进建议: " + (improvements.isEmpty() ? "无" : improvements.substring(0, Math.min(50, improvements.length())) + "..."));
        
        return result;
    }
    
    // 提取单个维度的评分和评语
    private Map<String, Object> extractDimension(String feedback, String dimensionName, String scorePattern) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        // 提取分数比例
        double ratio = 0.0;
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(scorePattern);
            java.util.regex.Matcher m = p.matcher(feedback);
            if (m.find()) {
                int score = Integer.parseInt(m.group(1));
                int maxScore = Integer.parseInt(m.group(2));
                ratio = (double) score / maxScore;
                System.out.println("提取维度分数: " + score + "/" + maxScore + " = " + ratio);
            }
        } catch (Exception e) {
            System.out.println("⚠️ 提取 " + dimensionName + " 分数失败: " + e.getMessage());
        }
        result.put("ratio", ratio);
        
        // 提取评语（在该维度标题到下一个标题之间）
        String comment = extractSection(feedback, dimensionName + "[：:：]?", 
                "(内容评分|结构评分|语言评分|创意评分|总体评价|改进建议|修改建议)");
        
        // ⭐ 后端兜底：过滤掉评语中的建议性文字
        comment = filterSuggestionWords(comment);
        
        result.put("comment", comment);
        
        // 提取指导建议（暂时为空，可以从评语中进一步解析）
        result.put("guide", "");
        
        return result;
    }
    
    // ⭐ 清理提取的文本（去掉括号说明、多余空格等）
    private String cleanExtractedText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 去掉括号及其内容（包括中文括号和英文括号）
        String cleaned = text.replaceAll("[（(][^)）]*[)）]", "");
        
        // 去掉多余的空白字符
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        return cleaned;
    }
    
    // ⭐ 过滤建议性词汇（后端兜底方案）
    private String filterSuggestionWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 移除包含建议性词汇的句子
        String[] suggestionKeywords = {
            "建议", "可以", "应当", "应该", "不妨", 
            "如果.*会更好", "如果.*将更", "最好", 
            "需要", "要", "尝试", "试着"
        };
        
        String filtered = text;
        
        // 按句子分割（中文句号、感叹号、问号）
        String[] sentences = filtered.split("[。！？\\n]+");
        StringBuilder cleanText = new StringBuilder();
        
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.isEmpty()) continue;
            
            // 检查是否包含建议性词汇
            boolean hasSuggestion = false;
            for (String keyword : suggestionKeywords) {
                if (sentence.matches(".*" + keyword + ".*")) {
                    hasSuggestion = true;
                    System.out.println("🧹 过滤掉建议性句子: " + sentence);
                    break;
                }
            }
            
            // 只保留非建议性的句子
            if (!hasSuggestion) {
                if (cleanText.length() > 0) {
                    cleanText.append("。");
                }
                cleanText.append(sentence);
            }
        }
        
        String result = cleanText.toString();
        if (!result.equals(text)) {
            System.out.println("✅ 评语过滤完成，原长度: " + text.length() + "，新长度: " + result.length());
        }
        
        return result;
    }
    
    // 提取两个标记之间的文本
    private String extractSection(String text, String startPattern, String endPattern) {
        try {
            java.util.regex.Pattern p;
            if (endPattern != null) {
                // ⭐ 修复：确保 startPattern 不包含捕获组，使用非捕获组 (?:...)
                // 这样捕获组1就是内容，而不是标题
                p = java.util.regex.Pattern.compile(startPattern + "\\s*([\\s\\S]*?)(?=" + endPattern + "|$)", 
                        java.util.regex.Pattern.CASE_INSENSITIVE);
            } else {
                p = java.util.regex.Pattern.compile(startPattern + "\\s*([\\s\\S]*?)$", 
                        java.util.regex.Pattern.CASE_INSENSITIVE);
            }
            java.util.regex.Matcher m = p.matcher(text);
            if (m.find()) {
                // ⭐ 始终返回最后一个捕获组（内容），因为 startPattern 应该用非捕获组
                // 计算总的捕获组数，返回最后一个（内容组）
                int groupCount = m.groupCount();
                if (groupCount > 0) {
                    return m.group(groupCount).trim();  // 返回最后一个捕获组
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ 提取文本段落失败: " + e.getMessage());
        }
        return "";
    }

    // ============== 默认数据 ==============
    private PracticeBook getDefaultBook() {
        PracticeBook b = new PracticeBook();
        b.setId(1L);
        b.setName("写人作文专练");
        return b;
    }

    private List<PracticeQuestion> getDefaultQuestions(Long bookId) {
        List<PracticeQuestion> list = new ArrayList<>();
        
        // 题目配置：标题 | 要求 | 类型
        String[][] questions = {
            {"我的妈妈", "不少于500字，注意人物描写", "SUBJECTIVE"},
            {"我的朋友", "不少于500字，写出朋友的特点", "SUBJECTIVE"},
            {"我最敬佩的人", "不少于500字，说明敬佩的理由", "SUBJECTIVE"},
            {"窗外的天空", "关于天空的观察与感悟，不少于500字", "SUBJECTIVE"},
            {"我的自画像", "不少于500字，展现真实的自己", "SUBJECTIVE"},
            {"勤劳的爷爷", "不少于500字，通过具体事例表现", "SUBJECTIVE"},
            {"爱笑的她", "不少于500字，突出人物性格特点", "SUBJECTIVE"},
            {"难忘的老师", "不少于500字，写出难忘的原因", "SUBJECTIVE"}
        };
        
        for (int i = 0; i < questions.length; i++) {
            PracticeQuestion q = new PracticeQuestion();
            q.setId((long) (i + 1));
            q.setBookId(bookId);
            q.setTitle(questions[i][0]);
            q.setRequirement(questions[i][1]);
            q.setType(questions[i][2]);
            q.setMaxScore(100);
            list.add(q);
        }
        return list;
    }

    private List<Map<String, Object>> getDefaultLibraries() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> a = new LinkedHashMap<>();
        a.put("id", 1);
        a.put("title", "小学三年级作文精选");
        a.put("description", "精选小学三年级优秀作文题目");
        a.put("author", "张老师");
        a.put("totalCount", 15);
        a.put("createdAt", LocalDateTime.now().minusDays(3).toString());
        list.add(a);

        Map<String, Object> b = new LinkedHashMap<>();
        b.put("id", 2);
        b.put("title", "记叙文专项训练");
        b.put("description", "记叙文写作专项训练");
        b.put("author", "李老师");
        b.put("totalCount", 12);
        b.put("createdAt", LocalDateTime.now().minusDays(5).toString());
        list.add(b);
        return list;
    }

    private List<Map<String, Object>> getDefaultSets() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> a = new LinkedHashMap<>();
        a.put("id", 1);
        a.put("name", "第一单元 写人作文专练");
        a.put("createTime", LocalDateTime.now().minusDays(1).toString());
        a.put("creator", "张老师");
        list.add(a);

        Map<String, Object> b = new LinkedHashMap<>();
        b.put("id", 2);
        b.put("name", "第二单元 写景作文训练");
        b.put("createTime", LocalDateTime.now().minusDays(2).toString());
        b.put("creator", "李老师");
        list.add(b);
        return list;
    }
}
