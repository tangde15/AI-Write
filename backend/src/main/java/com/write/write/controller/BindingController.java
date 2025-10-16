package com.write.write.controller;

import com.write.write.entity.StudentParent;
import com.write.write.entity.StudentTeacher;
import com.write.write.entity.UserAccount;
import com.write.write.repository.StudentParentRepository;
import com.write.write.repository.StudentTeacherRepository;
import com.write.write.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/binding")
@RequiredArgsConstructor
public class BindingController {

    private final StudentTeacherRepository studentTeacherRepository;
    private final StudentParentRepository studentParentRepository;
    private final UserRepository userRepository;

    /**
     * 教师绑定学生（通过用户名）
     */
    @PostMapping("/teacher/bind-student")
    public ResponseEntity<?> teacherBindStudent(@RequestBody Map<String, String> request, HttpSession session) {
        Long teacherId = (Long) session.getAttribute("userId");
        String studentUsername = request.get("studentUsername");
        
        if (teacherId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        UserAccount student = userRepository.findByUsername(studentUsername).orElse(null);
        if (student == null) {
            return ResponseEntity.badRequest().body("学生不存在");
        }
        
        if (!"STUDENT".equals(student.getRole())) {
            return ResponseEntity.badRequest().body("该用户不是学生");
        }
        
        // 检查是否已绑定
        if (studentTeacherRepository.existsByTeacherIdAndStudentId(teacherId, student.getId())) {
            return ResponseEntity.badRequest().body("已经绑定过该学生");
        }
        
        StudentTeacher binding = new StudentTeacher();
        binding.setTeacherId(teacherId);
        binding.setStudentId(student.getId());
        studentTeacherRepository.save(binding);
        
        return ResponseEntity.ok("绑定成功");
    }

    /**
     * 家长绑定孩子（通过用户名）
     */
    @PostMapping("/parent/bind-child")
    public ResponseEntity<?> parentBindChild(@RequestBody Map<String, String> request, HttpSession session) {
        Long parentId = (Long) session.getAttribute("userId");
        String studentUsername = request.get("studentUsername");
        
        if (parentId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        UserAccount student = userRepository.findByUsername(studentUsername).orElse(null);
        if (student == null) {
            return ResponseEntity.badRequest().body("学生不存在");
        }
        
        if (!"STUDENT".equals(student.getRole())) {
            return ResponseEntity.badRequest().body("该用户不是学生");
        }
        
        // 检查是否已绑定
        if (studentParentRepository.existsByParentIdAndStudentId(parentId, student.getId())) {
            return ResponseEntity.badRequest().body("已经绑定过该孩子");
        }
        
        StudentParent binding = new StudentParent();
        binding.setParentId(parentId);
        binding.setStudentId(student.getId());
        studentParentRepository.save(binding);
        
        return ResponseEntity.ok("绑定成功");
    }

    /**
     * 学生绑定教师（通过用户名）
     */
    @PostMapping("/student/bind-teacher")
    public ResponseEntity<?> studentBindTeacher(@RequestBody Map<String, String> request, HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        String teacherUsername = request.get("teacherUsername");
        
        if (studentId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        UserAccount teacher = userRepository.findByUsername(teacherUsername).orElse(null);
        if (teacher == null) {
            return ResponseEntity.badRequest().body("教师不存在");
        }
        
        if (!"TEACHER".equals(teacher.getRole())) {
            return ResponseEntity.badRequest().body("该用户不是教师");
        }
        
        if (studentTeacherRepository.existsByTeacherIdAndStudentId(teacher.getId(), studentId)) {
            return ResponseEntity.badRequest().body("已经绑定过该教师");
        }
        
        StudentTeacher binding = new StudentTeacher();
        binding.setTeacherId(teacher.getId());
        binding.setStudentId(studentId);
        studentTeacherRepository.save(binding);
        
        return ResponseEntity.ok("绑定成功");
    }

    /**
     * 学生绑定家长（通过用户名）
     */
    @PostMapping("/student/bind-parent")
    public ResponseEntity<?> studentBindParent(@RequestBody Map<String, String> request, HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");
        String parentUsername = request.get("parentUsername");
        
        if (studentId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        UserAccount parent = userRepository.findByUsername(parentUsername).orElse(null);
        if (parent == null) {
            return ResponseEntity.badRequest().body("家长不存在");
        }
        
        if (!"PARENT".equals(parent.getRole())) {
            return ResponseEntity.badRequest().body("该用户不是家长");
        }
        
        if (studentParentRepository.existsByParentIdAndStudentId(parent.getId(), studentId)) {
            return ResponseEntity.badRequest().body("已经绑定过该家长");
        }
        
        StudentParent binding = new StudentParent();
        binding.setParentId(parent.getId());
        binding.setStudentId(studentId);
        studentParentRepository.save(binding);
        
        return ResponseEntity.ok("绑定成功");
    }

    /**
     * 获取我的绑定列表
     */
    @GetMapping("/my-bindings")
    public ResponseEntity<?> getMyBindings(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        if ("TEACHER".equals(role)) {
            // 教师查看绑定的学生
            var bindings = studentTeacherRepository.findByTeacherId(userId);
            result.put("students", bindings.stream()
                .map(b -> userRepository.findById(b.getStudentId()).orElse(null))
                .filter(u -> u != null)
                .toList());
        } else if ("PARENT".equals(role)) {
            // 家长查看绑定的孩子
            var bindings = studentParentRepository.findByParentId(userId);
            result.put("children", bindings.stream()
                .map(b -> userRepository.findById(b.getStudentId()).orElse(null))
                .filter(u -> u != null)
                .toList());
        } else if ("STUDENT".equals(role)) {
            // 学生查看绑定的教师和家长
            var teacherBindings = studentTeacherRepository.findByStudentId(userId);
            var parentBindings = studentParentRepository.findByStudentId(userId);
            
            result.put("teachers", teacherBindings.stream()
                .map(b -> userRepository.findById(b.getTeacherId()).orElse(null))
                .filter(u -> u != null)
                .toList());
            
            result.put("parents", parentBindings.stream()
                .map(b -> userRepository.findById(b.getParentId()).orElse(null))
                .filter(u -> u != null)
                .toList());
        }
        
        return ResponseEntity.ok(result);
    }
}

