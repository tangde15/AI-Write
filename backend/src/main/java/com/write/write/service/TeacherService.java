package com.write.write.service;

import com.write.write.entity.StudentTeacher;
import com.write.write.entity.TeacherStudentBinding;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.StudentTeacherRepository;
import com.write.write.repository.TeacherStudentBindingRepository;
import com.write.write.repository.UserRepository;
import com.write.write.repository.WritingProgressRepository;
import com.write.write.repository.WritingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final StudentTeacherRepository studentTeacherRepository;
    private final TeacherStudentBindingRepository bindingRepository;
    private final UserRepository userRepository;
    private final WritingRecordRepository recordRepository;
    private final WritingProgressRepository progressRepository;

    /** 获取教师绑定的学生（包含作文数量） **/
    public List<StudentWithCount> getStudents(Long teacherId) {
        // 来源一：旧的 StudentTeacher 关系
        List<Long> ids1 = studentTeacherRepository.findByTeacherId(teacherId).stream()
                .map(StudentTeacher::getStudentId)
                .toList();
        // 来源二：新的绑定码关系 TeacherStudentBinding
        List<Long> ids2 = bindingRepository.findByTeacherId(teacherId).stream()
                .map(TeacherStudentBinding::getStudentId)
                .toList();

        // 合并去重
        Set<Long> allIds = new LinkedHashSet<>();
        allIds.addAll(ids1);
        allIds.addAll(ids2);

        List<StudentWithCount> result = new ArrayList<>();
        for (Long sid : allIds) {
            UserAccount student = userRepository.findById(sid).orElse(null);
            if (student == null) continue;
            int writingCount = recordRepository.findByUserOrderByCreatedAtDesc(student).size();
            result.add(new StudentWithCount(student.getId(), student.getUsername(), student.getRole(), writingCount));
        }
        return result;
    }
    
    /** 学生信息 DTO（包含作文数量） **/
    public record StudentWithCount(Long id, String username, String role, int writingCount) {}

    /** 获取学生作文记录 **/
    public List<WritingRecord> getStudentRecords(Long teacherId, Long studentId) {
        UserAccount student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        return recordRepository.findByUserOrderByCreatedAtDesc(student);
    }

    /** 获取学生进度 **/
    public List<WritingProgress> getStudentProgress(Long teacherId, Long studentId) {
        List<WritingProgress> progressList = progressRepository.findByStudentIdOrderByDateAsc(studentId);
        System.out.println("📊 教师查询学生进度 - 学生ID: " + studentId + ", 进度记录数: " + progressList.size());
        return progressList;
    }

    /** 教师批改作文 **/
    @Transactional
    public void addTeacherFeedback(Long teacherId, Long recordId, String feedback) {
        WritingRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("作文记录不存在"));
        record.setTeacherFeedback(feedback);
        record.setUpdatedAt(LocalDateTime.now());
        recordRepository.save(record);
    }
}
