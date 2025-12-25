package com.write.write.controller;

import com.write.write.dto.ConversationDTO;
import com.write.write.dto.MessageDTO;
import com.write.write.dto.SendMessageRequest;
import com.write.write.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendMessageRequest request, HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        MessageDTO dto = messageService.sendMessage(senderId, request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/conversation/{partnerId}")
    public ResponseEntity<?> conversation(@PathVariable Long partnerId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        List<MessageDTO> list = messageService.getConversation(userId, partnerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> conversations(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        List<ConversationDTO> list = messageService.getConversations(userId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/read/{messageId}")
    public ResponseEntity<?> markRead(@PathVariable Long messageId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        messageService.markAsRead(messageId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        long count = messageService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
}
