package com.write.write.controller;

import com.write.write.entity.TeacherStudentBinding;
import com.write.write.entity.UserAccount;
import com.write.write.repository.TeacherStudentBindingRepository;
import com.write.write.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherBindingController {

    private final UserRepository userRepository;
    private final TeacherStudentBindingRepository bindingRepository;

    @GetMapping("/binding/code")
    public ResponseEntity<Map<String, String>> getBindingCode(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserAccount teacher = userRepository.findById(userId).orElse(null);
        if (teacher == null || !"TEACHER".equalsIgnoreCase(teacher.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String code = teacher.getBindingCode();
        if (code == null || code.isEmpty()) {
            code = generateCode();
            teacher.setBindingCode(code);
            teacher.setBindingCodeUpdatedAt(LocalDateTime.now());
            userRepository.save(teacher);
        }
        return ResponseEntity.ok(Map.of("code", code));
    }

    @PostMapping("/binding/reset")
    public ResponseEntity<Map<String, String>> resetBindingCode(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserAccount teacher = userRepository.findById(userId).orElse(null);
        if (teacher == null || !"TEACHER".equalsIgnoreCase(teacher.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String code = generateCode();
        teacher.setBindingCode(code);
        teacher.setBindingCodeUpdatedAt(LocalDateTime.now());
        userRepository.save(teacher);
        return ResponseEntity.ok(Map.of("code", code));
    }

    @PostMapping("/unbind")
    public ResponseEntity<Void> unbindStudent(@RequestParam Long studentId, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        if (teacherId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        bindingRepository.deleteByTeacherIdAndStudentId(teacherId, studentId);
        return ResponseEntity.ok().build();
    }

    /** 学生端绑定入口 */
    @PostMapping("/bind/by-code")
    public ResponseEntity<?> bindByCode(@RequestParam String code, HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","UNAUTHORIZED","message","未登录"));
        UserAccount student = userRepository.findById(studentId).orElse(null);
        if (student == null || !"STUDENT".equalsIgnoreCase(student.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN","message","仅学生可绑定教师"));
        }

        if (code == null || code.trim().length() != 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_CODE","message","绑定码格式不正确"));
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);

        Optional<UserAccount> teacherOpt = userRepository.findByBindingCode(normalized)
                .filter(u -> "TEACHER".equalsIgnoreCase(u.getRole()));
        if (teacherOpt.isEmpty()) {
            // 兼容大小写查询
            teacherOpt = userRepository.findByBindingCodeIgnoreCase(normalized)
                    .filter(u -> "TEACHER".equalsIgnoreCase(u.getRole()));
        }
        if (teacherOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","CODE_NOT_FOUND","message","绑定码无效或教师未生成绑定码"));
        }
        UserAccount teacher = teacherOpt.get();
        // 多教师绑定：若该老师已与该学生绑定则直接成功返回，否则新增一条
        Optional<TeacherStudentBinding> exists = bindingRepository.findByTeacherIdAndStudentId(teacher.getId(), studentId);
        if (exists.isEmpty()) {
            TeacherStudentBinding binding = new TeacherStudentBinding();
            binding.setTeacherId(teacher.getId());
            binding.setStudentId(studentId);
            binding.setCreatedAt(LocalDateTime.now());
            bindingRepository.save(binding);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    private String generateCode() {
        String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去掉易混字符
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return sb.toString();
    }
}