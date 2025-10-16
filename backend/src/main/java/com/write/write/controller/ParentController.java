package com.write.write.controller;

import com.write.write.service.ParentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping("/bind")
    public ResponseEntity<String> bindStudent(@RequestParam Long studentId, HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        parentService.bindStudent(parentId, studentId);
        return ResponseEntity.ok("绑定成功");
    }

    @GetMapping("/children")
    public ResponseEntity<?> getChildren(HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getBoundStudents(parentId));
    }

    @GetMapping("/child/{childId}/writings")
    public ResponseEntity<?> getChildWritings(@PathVariable Long childId, HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getChildRecords(parentId, childId));
    }
    
    @GetMapping("/child/{childId}/progress")
    public ResponseEntity<?> getChildProgress(@PathVariable Long childId, HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getChildProgress(parentId, childId));
    }
    
    @GetMapping("/child/{childId}/encouragements")
    public ResponseEntity<?> getChildEncouragements(@PathVariable Long childId, HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getChildEncouragements(parentId, childId));
    }

    @GetMapping("/child/records")
    public ResponseEntity<?> getChildRecords(HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getChildRecords(parentId));
    }

    @GetMapping("/child/progress")
    public ResponseEntity<?> getChildProgress(HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(parentService.getChildProgressSummary(parentId));
    }
}
