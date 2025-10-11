package com.write.write.controller;

import com.write.write.dto.WritingRequest;
import com.write.write.dto.WritingResponse;
import com.write.write.entity.UserAccount;
import com.write.write.repository.UserRepository;
import com.write.write.service.WritingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingController {

    private final WritingService writingService;
    private final UserRepository userRepository;

    @PostMapping("/process")
    public ResponseEntity<WritingResponse> process(@RequestBody WritingRequest req, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserAccount user = userRepository.findById(userId).orElseThrow();
        String result = writingService.handleRequest(req, user);
        return ResponseEntity.ok(new WritingResponse(result));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserAccount user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(writingService.getHistory(user));
    }
}
