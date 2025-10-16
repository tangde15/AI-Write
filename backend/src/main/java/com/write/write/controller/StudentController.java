package com.write.write.controller;

import com.write.write.dto.WritingRequest;
import com.write.write.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitEssay(@RequestBody WritingRequest req, HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(studentService.submitEssay(studentId, req));
    }

    @GetMapping("/writings")
    public ResponseEntity<?> getWritings(HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(studentService.getWritingHistory(studentId));
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(studentService.getWritingHistory(studentId));
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(studentService.getProgress(studentId));
    }
    
    @GetMapping("/encouragements")
    public ResponseEntity<?> getEncouragements(HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(studentService.getEncouragements(studentId));
    }
}
