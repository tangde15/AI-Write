package com.write.write.controller;

import com.write.write.dto.LoginRequest;
import com.write.write.dto.RegisterRequest;
import com.write.write.entity.UserAccount;
import com.write.write.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        userService.register(req.getUsername(), req.getPassword(), req.getRole());
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpSession session) {
        UserAccount user = userService.verify(req.getUsername(), req.getPassword());
        if (user != null) {
            // 验证用户角色是否匹配
            if (req.getRole() != null && !req.getRole().equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户角色不匹配");
            }
            
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            
            // 返回前端需要的数据格式
            LoginResponse response = new LoginResponse(
                "session-token-" + user.getId(), // 简单的 token
                new UserInfo(user.getId(), user.getUsername(), user.getRole())
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        return ResponseEntity.ok(new UserInfoResponse(id, role));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("已登出");
    }

    record UserInfoResponse(Long userId, String role) {}
    
    record LoginResponse(String token, UserInfo user) {}
    
    record UserInfo(Long id, String username, String role) {}
}
