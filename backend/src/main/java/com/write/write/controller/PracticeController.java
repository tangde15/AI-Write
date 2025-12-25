package com.write.write.controller;

import com.write.write.entity.UserAccount;
import com.write.write.service.PracticeService;
import com.write.write.repository.PracticeAnswerRepository;
import com.write.write.repository.PracticeQuestionRepository;
import com.write.write.entity.PracticeAnswer;
import com.write.write.entity.PracticeQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {
    private final PracticeService practiceService;
    private final PracticeAnswerRepository answerRepo;
    private final PracticeQuestionRepository questionRepo;

    private Long currentUserId(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user instanceof UserAccount ua) return ua.getId();
        return 1L; // fallback 到 student1
    }

    // === 列表 ===
    @GetMapping("/libraries")
    public List<Map<String, Object>> libraries(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "12") int size) {
        return practiceService.listLibraries(page, size);
    }

    @GetMapping("/sets")
    public List<Map<String, Object>> sets(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return practiceService.listSets(page, size);
    }

    // === 题库/题单下的练习册列表 ===
    @GetMapping("/libraries/{libraryId}/books")
    public Map<String, Object> libraryBooks(@PathVariable Long libraryId, HttpSession session) {
        return practiceService.getLibraryBooks(libraryId, currentUserId(session));
    }

    @GetMapping("/sets/{setId}/books")
    public Map<String, Object> setBooks(@PathVariable Long setId, HttpSession session) {
        return practiceService.getSetBooks(setId, currentUserId(session));
    }

    // === 练习册 ===
    @GetMapping("/books/{bookId}")
    public Map<String, Object> book(@PathVariable Long bookId, HttpSession session) {
        return practiceService.getBookDetail(bookId, currentUserId(session));
    }

    @GetMapping("/books/{bookId}/questions")
    public List<Map<String, Object>> bookQuestions(@PathVariable Long bookId, HttpSession session) {
        return practiceService.getBookQuestions(bookId, currentUserId(session));
    }

    @GetMapping("/books/{bookId}/timeline")
    public List<Map<String, Object>> timeline(@PathVariable Long bookId, HttpSession session) {
        return practiceService.getBookTimeline(bookId, currentUserId(session));
    }

    // === 题目 ===
    @GetMapping("/questions/{id}")
    public Map<String, Object> question(@PathVariable Long id, HttpSession session) {
        return practiceService.getQuestionDetail(id, currentUserId(session));
    }

    @PostMapping("/questions/{id}/save")
    public Map<String, Object> save(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        practiceService.saveDraft(id, currentUserId(session), body.getOrDefault("answer", ""));
        return Map.of("success", true);
    }

    @PostMapping("/questions/{id}/submit")
    public Map<String, Object> submit(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        return practiceService.submit(id, currentUserId(session), body.getOrDefault("answer", ""));
    }

    @GetMapping("/questions/{id}/system-feedback")
    public Map<String, Object> systemFeedback(@PathVariable Long id, HttpSession session) {
        return practiceService.getSystemFeedbackByQuestion(id, currentUserId(session));
    }

    @GetMapping("/answers/{answerId}/status")
    public Map<String, Object> getAnswerStatus(@PathVariable Long answerId) {
        return practiceService.getAnswerStatus(answerId);
    }
    
    @GetMapping("/answers/{answerId}/system-feedback")
    public Map<String, Object> getAnswerSystemFeedback(@PathVariable Long answerId) {
        return practiceService.getSystemFeedback(answerId);
    }

    // 练习答案详情（含题目）——教师端重批使用
    @GetMapping("/answers/{answerId}/detail")
    public Map<String, Object> getAnswerDetail(@PathVariable Long answerId) {
        PracticeAnswer ans = answerRepo.findById(answerId).orElse(null);
        if (ans == null) {
            Map<String, Object> err = new java.util.HashMap<>();
            err.put("error", "not_found");
            return err;
        }
        PracticeQuestion q = questionRepo.findById(ans.getQuestionId()).orElse(null);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", ans.getId());
        result.put("questionId", ans.getQuestionId());
        result.put("content", ans.getContent());
        result.put("score", ans.getScore());
        result.put("aiFeedback", ans.getAiFeedback());
        result.put("teacherFeedback", ans.getTeacherFeedback());
        result.put("updatedAt", ans.getUpdatedAt());
        result.put("createdAt", ans.getCreatedAt());
        
        if (q != null) {
            Map<String, Object> qMap = new java.util.HashMap<>();
            qMap.put("id", q.getId());
            qMap.put("title", q.getTitle());
            qMap.put("requirement", q.getRequirement());
            result.put("question", qMap);
        } else {
            result.put("question", new java.util.HashMap<>());
        }
        
        return result;
    }

    @GetMapping("/questions/{id}/teacher-feedback")
    public Map<String, Object> teacherFeedback(@PathVariable Long id) {
        return Map.of("feedback", "老师还未批改");
    }

    @PostMapping("/questions/{id}/favorite")
    public Map<String, Object> favorite(@PathVariable Long id) {
        return Map.of("success", true);
    }
}
