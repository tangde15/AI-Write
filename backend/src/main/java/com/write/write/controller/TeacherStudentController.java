package com.write.write.controller;

import com.write.write.dto.AbilitySummary;
import com.write.write.dto.AbilityTimeseriesPoint;
import com.write.write.entity.UserAccount;
import com.write.write.entity.PracticeAnswer;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.UserRepository;
import com.write.write.repository.WritingRecordRepository;
import com.write.write.repository.TeacherStudentBindingRepository;
import com.write.write.repository.PracticeAnswerRepository;
import com.write.write.service.WritingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher/student")
@RequiredArgsConstructor
public class TeacherStudentController {

    private final TeacherStudentBindingRepository bindingRepository;
    private final UserRepository userRepository;
    private final WritingRecordRepository writingRecordRepository;
    private final PracticeAnswerRepository practiceAnswerRepository;
    private final WritingService writingService;

    private boolean isTeacher(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        if (id == null) return false;
        UserAccount u = userRepository.findById(id).orElse(null);
        return u != null && "TEACHER".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("/{studentId}/ability/summary")
    public ResponseEntity<AbilitySummary> getStudentSummary(@PathVariable Long studentId, HttpSession session) {
        if (!isTeacher(session)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserAccount student = userRepository.findById(studentId).orElse(null);
        if (student == null) return ResponseEntity.notFound().build();

        List<WritingRecord> records = writingRecordRepository.findByUserOrderByCreatedAtDesc(student);
        List<PracticeAnswer> answers = practiceAnswerRepository.findByUserIdOrderByUpdatedAtAsc(studentId);
        int total = records.size() + answers.size();

        List<Integer> overalls = new ArrayList<>();
        List<Integer> contents = new ArrayList<>();
        List<Integer> structures = new ArrayList<>();
        List<Integer> languages = new ArrayList<>();
        List<Integer> creativities = new ArrayList<>();

        for (WritingRecord r : records) {
            Integer overall = r.getTeacherOverallScore() != null ? r.getTeacherOverallScore() : r.getScore();
            if (overall != null) overalls.add(overall);
            Integer c = r.getTeacherContentScore();
            Integer s = r.getTeacherStructureScore();
            Integer l = r.getTeacherLanguageScore();
            Integer cr = r.getTeacherCreativityScore();
            if (c == null || s == null || l == null || cr == null) {
                Map<String, Integer> dims = writingService.extractDimensionScores(r.getAiResponse());
                c = c != null ? c : dims.get("content");
                s = s != null ? s : dims.get("structure");
                l = l != null ? l : dims.get("language");
                cr = cr != null ? cr : dims.get("creativity");
            }
            if (c != null) contents.add(c);
            if (s != null) structures.add(s);
            if (l != null) languages.add(l);
            if (cr != null) creativities.add(cr);
        }

        // 合并练习答案的分数（综合分 + 四维分从 aiFeedback 解析）
        for (PracticeAnswer a : answers) {
            if (a.getScore() != null) overalls.add(a.getScore());
            String src = (a.getTeacherFeedback() != null && !a.getTeacherFeedback().isBlank()) ? a.getTeacherFeedback() : a.getAiFeedback();
            Map<String, Integer> dims = writingService.extractDimensionScores(src);
            Integer c = dims.get("content");
            Integer s = dims.get("structure");
            Integer l = dims.get("language");
            Integer cr = dims.get("creativity");
            if (c != null) contents.add(c);
            if (s != null) structures.add(s);
            if (l != null) languages.add(l);
            if (cr != null) creativities.add(cr);
        }

        AbilitySummary summary = new AbilitySummary(
                total,
                avg(overalls),
                avg(contents),
                avg(structures),
                avg(languages),
                avg(creativities)
        );
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{studentId}/ability/timeseries")
    public ResponseEntity<List<AbilityTimeseriesPoint>> getStudentTimeseries(@PathVariable Long studentId,
                                                                             @RequestParam(required = false) String range,
                                                                             HttpSession session) {
        if (!isTeacher(session)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserAccount student = userRepository.findById(studentId).orElse(null);
        if (student == null) return ResponseEntity.notFound().build();

        List<WritingRecord> records = writingRecordRepository.findByUserOrderByCreatedAtDesc(student);
        List<PracticeAnswer> answers = practiceAnswerRepository.findByUserIdOrderByUpdatedAtAsc(studentId);

        // 时间范围过滤：week/month/term(90d)
        final LocalDateTime start =
            "week".equalsIgnoreCase(range) ? LocalDateTime.now().minusDays(7) :
            ("month".equalsIgnoreCase(range) ? LocalDateTime.now().minusDays(30) :
            ("term".equalsIgnoreCase(range) ? LocalDateTime.now().minusDays(90) : null));
        if (start != null) {
            records = records.stream().filter(r -> r.getCreatedAt() != null && r.getCreatedAt().isAfter(start)).collect(Collectors.toList());
            answers = answers.stream().filter(a -> {
                if (a.getUpdatedAt() != null) return a.getUpdatedAt().isAfter(start);
                return a.getCreatedAt() != null && a.getCreatedAt().isAfter(start);
            }).collect(Collectors.toList());
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<AbilityTimeseriesPoint> points = new ArrayList<>();
        for (WritingRecord r : records) {
            Integer overall = r.getTeacherOverallScore() != null ? r.getTeacherOverallScore() : r.getScore();
            Integer c = r.getTeacherContentScore();
            Integer s = r.getTeacherStructureScore();
            Integer l = r.getTeacherLanguageScore();
            Integer cr = r.getTeacherCreativityScore();
            if (c == null || s == null || l == null || cr == null) {
                Map<String, Integer> dims = writingService.extractDimensionScores(r.getAiResponse());
                c = c != null ? c : dims.get("content");
                s = s != null ? s : dims.get("structure");
                l = l != null ? l : dims.get("language");
                cr = cr != null ? cr : dims.get("creativity");
            }
            points.add(new AbilityTimeseriesPoint(
                    r.getId(),
                    r.getCreatedAt() != null ? r.getCreatedAt().format(fmt) : "",
                    r.getTopic(),
                    overall,
                    c, s, l, cr
            ));
        }

        // 合并练习答案时间序列
        for (PracticeAnswer a : answers) {
            String src = (a.getTeacherFeedback() != null && !a.getTeacherFeedback().isBlank()) ? a.getTeacherFeedback() : a.getAiFeedback();
            Map<String, Integer> dims = writingService.extractDimensionScores(src);
            String date = a.getUpdatedAt() != null ? a.getUpdatedAt().format(fmt) : (a.getCreatedAt() != null ? a.getCreatedAt().format(fmt) : "");
            points.add(new AbilityTimeseriesPoint(
                    a.getId(),
                    date,
                    "练习",
                    a.getScore(),
                    dims.get("content"),
                    dims.get("structure"),
                    dims.get("language"),
                    dims.get("creativity")
            ));
        }

        // 按时间升序（允许空值最后）
        points.sort(Comparator.comparing(AbilityTimeseriesPoint::getDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return ResponseEntity.ok(points);
    }

    private double avg(List<Integer> list) {
        if (list == null || list.isEmpty()) return 0.0;
        return Math.round(list.stream().mapToInt(Integer::intValue).average().orElse(0.0) * 100.0) / 100.0;
    }

    // 合并学生“提交”（作文+练习答案）列表，供教师端展示
    @GetMapping("/{studentId}/submissions")
    public ResponseEntity<List<Map<String, Object>>> getStudentSubmissions(@PathVariable Long studentId,
                                                                           HttpSession session) {
        if (!isTeacher(session)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserAccount student = userRepository.findById(studentId).orElse(null);
        if (student == null) return ResponseEntity.notFound().build();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> items = new ArrayList<>();

        // 写作记录
        List<WritingRecord> records = writingRecordRepository.findByUserOrderByCreatedAtDesc(student);
        for (WritingRecord r : records) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", r.getId());
            m.put("type", "WRITING");
            m.put("title", Optional.ofNullable(r.getTopic()).orElse("未命题"));
            m.put("date", r.getCreatedAt() != null ? r.getCreatedAt().format(fmt) : "");
            m.put("score", Optional.ofNullable(r.getTeacherOverallScore()).orElse(r.getScore()));
            m.put("brief", Optional.ofNullable(r.getEssay()).orElse(""));
            items.add(m);
        }

        // 练习答案
        List<PracticeAnswer> answers = practiceAnswerRepository.findByUserIdOrderByUpdatedAtAsc(studentId);
        for (PracticeAnswer a : answers) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", a.getId());
            m.put("type", "PRACTICE");
            m.put("title", "练习");
            String date = a.getUpdatedAt() != null ? a.getUpdatedAt().format(fmt) : (a.getCreatedAt() != null ? a.getCreatedAt().format(fmt) : "");
            m.put("date", date);
            m.put("score", a.getScore());
            m.put("brief", Optional.ofNullable(a.getContent()).orElse(""));
            items.add(m);
        }

        // 按时间倒序
        items.sort(Comparator.comparing((Map<String, Object> m) -> (String)m.get("date"), Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return ResponseEntity.ok(items);
    }
}