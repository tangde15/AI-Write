package com.write.write.controller;

import com.write.write.entity.PracticeAnswer;
import com.write.write.repository.PracticeAnswerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/teacher/practice")
@RequiredArgsConstructor
public class TeacherPracticeReviewController {

    private final PracticeAnswerRepository answerRepo;

    @PutMapping("/{id}/review")
    public ResponseEntity<Void> review(@PathVariable Long id, @RequestBody ReviewPayload payload, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        PracticeAnswer ans = answerRepo.findById(id).orElse(null);
        if (ans == null) return ResponseEntity.notFound().build();

        int content30 = scale(payload.content, 30);
        int structure20 = scale(payload.structure, 20);
        int language30 = scale(payload.language, 30);
        int creativity20 = scale(payload.creativity, 20);
        int overall = (payload.content * 30 + payload.structure * 20 + payload.language * 30 + payload.creativity * 20) / 100;

        // 以解析友好的格式写入 teacherFeedback，供前端/统计复用
        StringBuilder sb = new StringBuilder();
        sb.append("内容评分：").append(content30).append("/30分\n");
        sb.append("结构评分：").append(structure20).append("/20分\n");
        sb.append("语言评分：").append(language30).append("/30分\n");
        sb.append("创意评分：").append(creativity20).append("/20分\n");
        if (payload.suggestions != null && !payload.suggestions.isBlank()) {
            sb.append("改进建议：\n").append(payload.suggestions.trim());
        }
        ans.setTeacherFeedback(sb.toString());
        ans.setScore(overall); // 学生端显示最终分
        ans.setUpdatedAt(LocalDateTime.now());
        answerRepo.save(ans);
        return ResponseEntity.ok().build();
    }

    private int scale(Integer v, int base) { return (v == null ? 0 : v) * base / 100; }

    @Data
    public static class ReviewPayload {
        public Integer content;
        public Integer structure;
        public Integer language;
        public Integer creativity;
        public String suggestions;
    }
}
