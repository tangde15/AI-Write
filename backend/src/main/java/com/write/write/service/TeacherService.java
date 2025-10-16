package com.write.write.service;

import com.write.write.entity.StudentTeacher;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.StudentTeacherRepository;
import com.write.write.repository.UserRepository;
import com.write.write.repository.WritingProgressRepository;
import com.write.write.repository.WritingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final StudentTeacherRepository studentTeacherRepository;
    private final UserRepository userRepository;
    private final WritingRecordRepository recordRepository;
    private final WritingProgressRepository progressRepository;

    /** 获取教师绑定的学生（包含作文数量） **/
    public List<StudentWithCount> getStudents(Long teacherId) {
        return studentTeacherRepository.findByTeacherId(teacherId).stream()
                .map(rel -> {
                    UserAccount student = userRepository.findById(rel.getStudentId()).orElse(null);
                    if (student == null) return null;
                    
                    // 统计该学生的作文数量
                    int writingCount = recordRepository.findByUserOrderByCreatedAtDesc(student).size();
                    
                    return new StudentWithCount(
                        student.getId(),
                        student.getUsername(),
                        student.getRole(),
                        writingCount
                    );
                })
                .filter(s -> s != null)
                .toList();
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
