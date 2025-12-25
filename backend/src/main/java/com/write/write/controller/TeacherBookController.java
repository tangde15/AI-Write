package com.write.write.controller;

import com.write.write.entity.BookAssignment;
import com.write.write.entity.PracticeQuestion;
import com.write.write.repository.BookAssignmentRepository;
import com.write.write.repository.PracticeQuestionRepository;
import com.write.write.repository.PracticeBookRepository;
import com.write.write.entity.PracticeBook;
import com.write.write.repository.UserRepository;
import com.write.write.entity.UserAccount;
import com.write.write.repository.TeacherStudentBindingRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/teacher/books")
@RequiredArgsConstructor
public class TeacherBookController {

    private final PracticeBookRepository bookRepo;
    private final PracticeQuestionRepository questionRepo;
    private final BookAssignmentRepository assignmentRepo;
    private final UserRepository userRepository;
    private final TeacherStudentBindingRepository bindingRepo;

    private UserAccount currentTeacher(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return null;
        UserAccount u = userRepository.findById(userId).orElse(null);
        if (u != null && "TEACHER".equalsIgnoreCase(u.getRole())) return u;
        return null;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeBook b = new PracticeBook();
        b.setName(Objects.toString(body.getOrDefault("name", "新题单")));
        b.setCreatorId(teacher.getId());
        b.setGradeTag(Objects.toString(body.getOrDefault("gradeTag", "")));
        b.setTopicTags(Objects.toString(body.getOrDefault("topicTags", "")));
        b.setStatus("DRAFT");
        b.setIsDraft(true);
        b.setCreatedAt(LocalDateTime.now());
        bookRepo.save(b);
        return ResponseEntity.ok(Map.of("id", b.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> myBooks(HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<PracticeBook> list = bookRepo.findByCreatorIdOrderByCreatedAtDesc(teacher.getId());
        List<Map<String, Object>> resp = new ArrayList<>();
        for (PracticeBook b : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", b.getId());
            m.put("name", b.getName());
            m.put("status", b.getStatus());
            m.put("questionCount", questionRepo.countByBookId(b.getId()));
            m.put("pushedCount", assignmentRepo.findByBookId(b.getId()).size());
            resp.add(m);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable Long bookId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeBook b = bookRepo.findById(bookId).orElse(null);
        if (b == null || !b.getCreatorId().equals(teacher.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> m = new HashMap<>();
        m.put("id", b.getId());
        m.put("name", b.getName());
        m.put("gradeTag", b.getGradeTag());
        m.put("topicTags", b.getTopicTags());
        m.put("status", b.getStatus());
        return ResponseEntity.ok(m);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> update(@PathVariable Long bookId, @RequestBody Map<String, Object> body, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeBook b = bookRepo.findById(bookId).orElse(null);
        if (b == null || !b.getCreatorId().equals(teacher.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (body.containsKey("name")) b.setName(Objects.toString(body.get("name")));
        if (body.containsKey("gradeTag")) b.setGradeTag(Objects.toString(body.get("gradeTag")));
        if (body.containsKey("topicTags")) b.setTopicTags(Objects.toString(body.get("topicTags")));
        bookRepo.save(b);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> delete(@PathVariable Long bookId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeBook b = bookRepo.findById(bookId).orElse(null);
        if (b == null || !b.getCreatorId().equals(teacher.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookRepo.deleteById(bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}/questions")
    public ResponseEntity<List<Map<String, Object>>> questions(@PathVariable Long bookId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<PracticeQuestion> qs = questionRepo.findByBookIdOrderByIdAsc(bookId);
        List<Map<String, Object>> resp = new ArrayList<>();
        for (PracticeQuestion q : qs) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", q.getId());
            m.put("title", q.getTitle());
            m.put("requirement", q.getRequirement());
            resp.add(m);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{bookId}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable Long bookId, @RequestBody Map<String, Object> body, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        PracticeQuestion q = new PracticeQuestion();
        q.setBookId(bookId);
        q.setTitle(Objects.toString(body.getOrDefault("title", "题目")));
        q.setRequirement(Objects.toString(body.getOrDefault("requirement", "")));
        q.setType("SUBJECTIVE");
        q.setMaxScore(100);
        q.setCreatedAt(LocalDateTime.now());
        questionRepo.save(q);
        return ResponseEntity.ok(Map.of("id", q.getId()));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        questionRepo.deleteById(questionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}/push/preview")
    public ResponseEntity<List<Map<String, Object>>> pushPreview(@PathVariable Long bookId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<Long> boundStudentIds = bindingRepo.findByTeacherId(teacher.getId()).stream()
                .map(b -> b.getStudentId())
                .distinct()
                .toList();
        List<Map<String, Object>> resp = new ArrayList<>();
        for (Long studentId : boundStudentIds) {
            UserAccount student = userRepository.findById(studentId).orElse(null);
            if (student != null && "STUDENT".equalsIgnoreCase(student.getRole())) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", student.getId());
                m.put("username", student.getUsername());
                m.put("account", student.getAccount());
                boolean alreadyPushed = assignmentRepo.findByBookIdAndStudentId(bookId, student.getId()).isPresent();
                m.put("alreadyPushed", alreadyPushed);
                resp.add(m);
            }
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{bookId}/push")
    public ResponseEntity<Map<String, Object>> push(@PathVariable Long bookId, @RequestBody Map<String, Object> body, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<Integer> studentIds = (List<Integer>) body.getOrDefault("studentIds", List.of());
        int success = 0;
        for (Integer sid : studentIds) {
            Long studentId = sid.longValue();
            if (assignmentRepo.findByBookIdAndStudentId(bookId, studentId).isEmpty()) {
                BookAssignment ba = new BookAssignment();
                ba.setBookId(bookId);
                ba.setTeacherId(teacher.getId());
                ba.setStudentId(studentId);
                ba.setPushedAt(LocalDateTime.now());
                ba.setStatus("PENDING");
                assignmentRepo.save(ba);
                success++;
            }
        }
        return ResponseEntity.ok(Map.of("count", success, "success", success));
    }

    @GetMapping("/{bookId}/push-records")
    public ResponseEntity<List<Map<String, Object>>> pushRecords(@PathVariable Long bookId, HttpSession session) {
        UserAccount teacher = currentTeacher(session);
        if (teacher == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<BookAssignment> records = assignmentRepo.findByBookId(bookId);
        List<Map<String, Object>> resp = new ArrayList<>();
        for (BookAssignment r : records) {
            UserAccount student = userRepository.findById(r.getStudentId()).orElse(null);
            if (student != null) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.getId());
                m.put("studentName", student.getUsername());
                m.put("status", r.getStatus());
                m.put("pushedAt", r.getPushedAt());
                m.put("readAt", r.getReadAt());
                m.put("completedAt", r.getCompletedAt());
                resp.add(m);
            }
        }
        return ResponseEntity.ok(resp);
    }
}
