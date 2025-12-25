package com.write.write.controller;

import com.write.write.entity.TeacherStudentBinding;
import com.write.write.entity.UserAccount;
import com.write.write.repository.TeacherStudentBindingRepository;
import com.write.write.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentBindingController {

    private final TeacherStudentBindingRepository bindingRepository;
    private final UserRepository userRepository;

    @GetMapping("/bound-teachers")
    public ResponseEntity<List<Map<String, Object>>> getBoundTeachers(HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserAccount student = userRepository.findById(studentId).orElse(null);
        if (student == null || !"STUDENT".equalsIgnoreCase(student.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<TeacherStudentBinding> bindings = bindingRepository.findByStudentId(studentId);
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (TeacherStudentBinding b : bindings) {
            UserAccount t = userRepository.findById(b.getTeacherId()).orElse(null);
            if (t != null) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", t.getId());
                m.put("username", Optional.ofNullable(t.getUsername()).orElse("教师"));
                m.put("account", t.getAccount());
                m.put("bindingAt", b.getCreatedAt() != null ? b.getCreatedAt().format(fmt) : "");
                list.add(m);
            }
        }
        return ResponseEntity.ok(list);
    }
}
