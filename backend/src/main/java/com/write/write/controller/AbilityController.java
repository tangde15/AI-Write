package com.write.write.controller;

import com.write.write.dto.AbilitySummary;
import com.write.write.dto.AbilityTimeseriesPoint;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.UserRepository;
import com.write.write.repository.PracticeAnswerRepository;
import com.write.write.entity.PracticeAnswer;
import com.write.write.repository.WritingRecordRepository;
import com.write.write.service.WritingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ability")
@RequiredArgsConstructor
public class AbilityController {

    private final WritingRecordRepository writingRecordRepository;
    private final UserRepository userRepository;
    private final WritingService writingService;
    private final PracticeAnswerRepository practiceAnswerRepository;

    @GetMapping("/summary")
    public ResponseEntity<AbilitySummary> getSummary(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserAccount user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<WritingRecord> records = writingRecordRepository.findByUserOrderByCreatedAtDesc(user);
        List<PracticeAnswer> answers = practiceAnswerRepository.findByUserIdOrderByUpdatedAtAsc(userId);

        int total = records.size() + answers.size();

        List<Integer> allScores = new ArrayList<>();
        // 写作记录综合分
        allScores.addAll(records.stream().map(WritingRecord::getScore).filter(Objects::nonNull).collect(Collectors.toList()));
        // 练习答案综合分
        allScores.addAll(answers.stream().map(PracticeAnswer::getScore).filter(Objects::nonNull).collect(Collectors.toList()));
        double avgScore = avg(allScores);

        List<Integer> contents = new ArrayList<>();
        List<Integer> structures = new ArrayList<>();
        List<Integer> languages = new ArrayList<>();
        List<Integer> creativities = new ArrayList<>();

        // 来自写作记录的四维评分（解析aiResponse）
        for (WritingRecord r : records) {
            Map<String, Integer> dims = writingService.extractDimensionScores(r.getAiResponse());
            addIfNotNull(contents, dims.get("content"));
            addIfNotNull(structures, dims.get("structure"));
            addIfNotNull(languages, dims.get("language"));
            addIfNotNull(creativities, dims.get("creativity"));
        }
        // 来自练习答案的四维评分（解析aiFeedback）
        for (PracticeAnswer a : answers) {
            String src = (a.getTeacherFeedback() != null && !a.getTeacherFeedback().isBlank()) ? a.getTeacherFeedback() : a.getAiFeedback();
            Map<String, Integer> dims = writingService.extractDimensionScores(src);
            addIfNotNull(contents, dims.get("content"));
            addIfNotNull(structures, dims.get("structure"));
            addIfNotNull(languages, dims.get("language"));
            addIfNotNull(creativities, dims.get("creativity"));
        }

        AbilitySummary summary = new AbilitySummary(
                total,
                round2(avgScore),
                round2(avg(contents)),
                round2(avg(structures)),
                round2(avg(languages)),
                round2(avg(creativities))
        );
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/timeseries")
    public ResponseEntity<List<AbilityTimeseriesPoint>> getTimeseries(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserAccount user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<WritingRecord> records = writingRecordRepository.findByUserOrderByCreatedAtDesc(user);
        List<PracticeAnswer> answers = practiceAnswerRepository.findByUserIdOrderByUpdatedAtAsc(userId);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<AbilityTimeseriesPoint> points = new ArrayList<>();

        for (WritingRecord r : records) {
            Map<String, Integer> dims = writingService.extractDimensionScores(r.getAiResponse());
            points.add(new AbilityTimeseriesPoint(
                    r.getId(),
                    r.getCreatedAt() != null ? r.getCreatedAt().format(fmt) : "",
                    r.getTopic(),
                    r.getScore(),
                    dims.get("content"),
                    dims.get("structure"),
                    dims.get("language"),
                    dims.get("creativity")
            ));
        }

        for (PracticeAnswer a : answers) {
            String src = (a.getTeacherFeedback() != null && !a.getTeacherFeedback().isBlank()) ? a.getTeacherFeedback() : a.getAiFeedback();
            Map<String, Integer> dims = writingService.extractDimensionScores(src);
            points.add(new AbilityTimeseriesPoint(
                    a.getId(),
                    a.getUpdatedAt() != null ? a.getUpdatedAt().format(fmt) : (a.getCreatedAt() != null ? a.getCreatedAt().format(fmt) : ""),
                    "练习",
                    a.getScore(),
                    dims.get("content"),
                    dims.get("structure"),
                    dims.get("language"),
                    dims.get("creativity")
            ));
        }

        // 升序按日期
        points.sort(Comparator.comparing(AbilityTimeseriesPoint::getDate, Comparator.nullsLast(Comparator.naturalOrder())));

        return ResponseEntity.ok(points);
    }

    private void addIfNotNull(List<Integer> list, Integer v) { if (v != null) list.add(v); }
    private double avg(List<Integer> list) {
        if (list == null || list.isEmpty()) return 0.0;
        return list.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}
