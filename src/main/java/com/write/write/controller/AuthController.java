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
        userService.register(req.getUsername(), req.getPassword());
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req, HttpSession session) {
        UserAccount user = userService.verify(req.getUsername(), req.getPassword());
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok("登录成功");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("已登出");
    }
}
