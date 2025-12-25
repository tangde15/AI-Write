package com.write.write.controller;

import com.write.write.entity.WritingRecord;
import com.write.write.repository.WritingRecordRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/teacher/writing")
@RequiredArgsConstructor
public class TeacherReviewController {

    private final WritingRecordRepository writingRecordRepository;

    @PutMapping("/{id}/review")
    public ResponseEntity<Void> review(@PathVariable Long id, @RequestBody ReviewRequest req, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // 简化权限：只要是教师即可（可扩展为仅绑定关系教师）
        WritingRecord r = writingRecordRepository.findById(id).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();

        r.setTeacherContentScore(req.content);
        r.setTeacherStructureScore(req.structure);
        r.setTeacherLanguageScore(req.language);
        r.setTeacherCreativityScore(req.creativity);

        // 综合分：内容30 + 结构20 + 语言30 + 创意20
        int overall = safe(req.content) * 30 + safe(req.structure) * 20 + safe(req.language) * 30 + safe(req.creativity) * 20;
        overall = overall / 100; // 转换到百分制
        r.setTeacherOverallScore(overall);

        if (req.suggestions != null) {
            r.setTeacherFeedback(req.suggestions);
        }
        r.setUpdatedAt(LocalDateTime.now());
        r.setTeacherReviewedAt(LocalDateTime.now());
        // 学生端显示教师最终分：覆盖主分数
        r.setScore(overall);

        writingRecordRepository.save(r);
        return ResponseEntity.ok().build();
    }

    private int safe(Integer v) { return v == null ? 0 : v; }

    @Data
    public static class ReviewRequest {
        public Integer content;
        public Integer structure;
        public Integer language;
        public Integer creativity;
        public String suggestions;
    }
}