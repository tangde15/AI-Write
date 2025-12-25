package com.write.write.controller;

import com.write.write.entity.BookAssignment;
import com.write.write.entity.PracticeBook;
import com.write.write.entity.PracticeQuestion;
import com.write.write.repository.BookAssignmentRepository;
import com.write.write.repository.PracticeBookRepository;
import com.write.write.repository.PracticeQuestionRepository;
import com.write.write.repository.UserRepository;
import com.write.write.entity.UserAccount;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/student/books")
@RequiredArgsConstructor
public class StudentBookController {

    private final BookAssignmentRepository assignmentRepo;
    private final PracticeBookRepository bookRepo;
    private final PracticeQuestionRepository questionRepo;
    private final UserRepository userRepository;

    private UserAccount currentStudent(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return null;
        UserAccount u = userRepository.findById(userId).orElse(null);
        if (u != null && "STUDENT".equalsIgnoreCase(u.getRole())) return u;
        return null;
    }

    @GetMapping("/pushed")
    public ResponseEntity<List<Map<String, Object>>> myPushedBooks(HttpSession session) {
        UserAccount student = currentStudent(session);
        if (student == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<BookAssignment> assigns = assignmentRepo.findByStudentId(student.getId());
        List<Map<String, Object>> resp = new ArrayList<>();
        for (BookAssignment a : assigns) {
            PracticeBook b = bookRepo.findById(a.getBookId()).orElse(null);
            if (b == null) continue;
            Map<String, Object> m = new HashMap<>();
            m.put("bookId", b.getId());
            m.put("name", b.getName());
            m.put("pushedAt", a.getPushedAt());
            m.put("status", a.getStatus());
            resp.add(m);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> bookDetail(@PathVariable Long bookId, HttpSession session) {
        UserAccount student = currentStudent(session);
        if (student == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeBook b = bookRepo.findById(bookId).orElse(null);
        if (b == null) return ResponseEntity.notFound().build();
        Map<String, Object> m = new HashMap<>();
        m.put("id", b.getId());
        m.put("name", b.getName());
        m.put("gradeTag", b.getGradeTag());
        m.put("topicTags", b.getTopicTags());
        List<Map<String, Object>> qs = new ArrayList<>();
        for (PracticeQuestion q : questionRepo.findByBookIdOrderByIdAsc(bookId)) {
            Map<String, Object> qi = new HashMap<>();
            qi.put("id", q.getId());
            qi.put("title", q.getTitle());
            qi.put("requirement", q.getRequirement());
            qs.add(qi);
        }
        m.put("questions", qs);
        return ResponseEntity.ok(m);
    }
}
