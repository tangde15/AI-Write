package com.write.write.controller;

import com.write.write.entity.EncouragementMessage;
import com.write.write.service.EncouragementService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EncouragementController {

    private final EncouragementService encouragementService;

    @GetMapping("/encourage/student/{studentId}")
    public ResponseEntity<List<EncouragementMessage>> getMessages(@PathVariable Long studentId) {
        return ResponseEntity.ok(encouragementService.getMessages(studentId));
    }

    @PostMapping("/encourage/student/{studentId}")
    public ResponseEntity<EncouragementMessage> addMessage(
            @PathVariable Long studentId,
            @RequestBody MessageRequest req,
            HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        EncouragementMessage msg = encouragementService.addManualMessage(senderId, studentId, role, req.content());
        return ResponseEntity.ok(msg);
    }

    // 统一的发送激励语接口（供教师端和家长端使用）
    @PostMapping("/encouragement/send")
    public ResponseEntity<?> sendEncouragement(
            @RequestBody SendEncouragementRequest req,
            HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        
        if (senderId == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        EncouragementMessage msg = encouragementService.addManualMessage(
            senderId, 
            req.studentId(), 
            req.fromRole() != null ? req.fromRole() : role, 
            req.content()
        );
        return ResponseEntity.ok(msg);
    }
    
    // 获取激励语列表
    @GetMapping("/encouragement/list")
    public ResponseEntity<?> getEncouragementList(
            @RequestParam(required = false) Long studentId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        // 如果是学生，查看自己的激励语
        Long targetStudentId = "STUDENT".equals(role) ? userId : studentId;
        
        if (targetStudentId == null) {
            return ResponseEntity.badRequest().body("请指定学生ID");
        }
        
        return ResponseEntity.ok(encouragementService.getMessages(targetStudentId));
    }

    record MessageRequest(String content) {}
    record SendEncouragementRequest(Long studentId, String content, String fromRole) {}
}
