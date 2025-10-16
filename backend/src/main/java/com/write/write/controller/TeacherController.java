package com.write.write.controller;

import com.write.write.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/students")
    public ResponseEntity<?> getMyStudents(HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(teacherService.getStudents(teacherId));
    }

    @GetMapping("/students/{studentId}/records")
    public ResponseEntity<?> getStudentRecords(@PathVariable Long studentId, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(teacherService.getStudentRecords(teacherId, studentId));
    }
    
    @GetMapping("/students/{studentId}/progress")
    public ResponseEntity<?> getStudentProgress(@PathVariable Long studentId, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(teacherService.getStudentProgress(teacherId, studentId));
    }

    @PostMapping("/feedback/{recordId}")
    public ResponseEntity<?> addFeedback(
            @PathVariable Long recordId,
            @RequestBody FeedbackRequest req,
            HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        teacherService.addTeacherFeedback(teacherId, recordId, req.feedback());
        return ResponseEntity.ok("批改完成");
    }

    record FeedbackRequest(String feedback) {}
}
