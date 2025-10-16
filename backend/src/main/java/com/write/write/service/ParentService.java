package com.write.write.service;

import com.write.write.entity.EncouragementMessage;
import com.write.write.entity.StudentParent;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.EncouragementRepository;
import com.write.write.repository.StudentParentRepository;
import com.write.write.repository.UserRepository;
import com.write.write.repository.WritingRecordRepository;
import com.write.write.repository.WritingProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final StudentParentRepository studentParentRepository;
    private final UserRepository userRepository;
    private final WritingRecordRepository recordRepository;
    private final WritingProgressRepository progressRepository;
    private final EncouragementRepository encouragementRepository;

    @Transactional
    public void bindStudent(Long parentId, Long studentId) {
        if (!studentParentRepository.findByParentId(parentId).isEmpty())
            throw new RuntimeException("家长已绑定孩子");
        if (!studentParentRepository.findByStudentId(studentId).isEmpty())
            throw new RuntimeException("学生已被绑定");

        StudentParent rel = new StudentParent();
        rel.setParentId(parentId);
        rel.setStudentId(studentId);
        studentParentRepository.save(rel);
    }

    /** 获取家长绑定的孩子列表（包含作文数量） **/
    public List<ChildWithCount> getBoundStudents(Long parentId) {
        List<StudentParent> relations = studentParentRepository.findByParentId(parentId);
        return relations.stream()
                .map(rel -> {
                    UserAccount student = userRepository.findById(rel.getStudentId()).orElse(null);
                    if (student == null) return null;
                    
                    // 统计该学生的作文数量
                    int writingCount = recordRepository.findByUserOrderByCreatedAtDesc(student).size();
                    
                    return new ChildWithCount(
                        student.getId(),
                        student.getUsername(),
                        student.getRole(),
                        writingCount
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }
    
    /** 孩子信息 DTO（包含作文数量） **/
    public record ChildWithCount(Long id, String username, String role, int writingCount) {}

    /** 获取孩子的作文记录（带childId参数） **/
    public List<WritingRecord> getChildRecords(Long parentId, Long childId) {
        // 验证绑定关系
        verifyBinding(parentId, childId);
        UserAccount student = userRepository.findById(childId).orElseThrow(() -> new RuntimeException("学生不存在"));
        return recordRepository.findByUserOrderByCreatedAtDesc(student);
    }
    
    /** 获取孩子的成长曲线（返回 List<WritingProgress>） **/
    public List<WritingProgress> getChildProgress(Long parentId, Long childId) {
        // 验证绑定关系
        verifyBinding(parentId, childId);
        List<WritingProgress> progressList = progressRepository.findByStudentIdOrderByDateAsc(childId);
        System.out.println("📊 家长查询孩子进度 - 孩子ID: " + childId + ", 进度记录数: " + progressList.size());
        return progressList;
    }
    
    /** 获取孩子收到的激励语 **/
    public List<EncouragementMessage> getChildEncouragements(Long parentId, Long childId) {
        // 验证绑定关系
        verifyBinding(parentId, childId);
        return encouragementRepository.findByStudentIdOrderByCreatedAtDesc(childId);
    }
    
    /** 验证家长-孩子绑定关系 **/
    private void verifyBinding(Long parentId, Long childId) {
        List<StudentParent> relations = studentParentRepository.findByParentId(parentId);
        boolean isBound = relations.stream().anyMatch(rel -> rel.getStudentId().equals(childId));
        if (!isBound) {
            throw new RuntimeException("未绑定该学生");
        }
    }

    /** 获取孩子的作文记录（兼容旧接口，取第一个孩子） **/
    public List<WritingRecord> getChildRecords(Long parentId) {
        List<StudentParent> relations = studentParentRepository.findByParentId(parentId);
        if (relations.isEmpty()) {
            throw new RuntimeException("未绑定学生");
        }
        Long studentId = relations.get(0).getStudentId();
        UserAccount student = userRepository.findById(studentId).orElseThrow();
        return recordRepository.findByUserOrderByCreatedAtDesc(student);
    }

    /** 获取孩子的进度统计摘要（兼容旧接口） **/
    public Map<String, Object> getChildProgressSummary(Long parentId) {
        List<StudentParent> relations = studentParentRepository.findByParentId(parentId);
        if (relations.isEmpty()) {
            throw new RuntimeException("未绑定学生");
        }
        Long studentId = relations.get(0).getStudentId();
        var progressList = progressRepository.findByStudentIdOrderByDateAsc(studentId);
        if (progressList.isEmpty()) return Map.of("message", "暂无统计数据");

        double avg = progressList.stream().mapToDouble(p -> p.getAvgScore() == null ? 0 : p.getAvgScore()).average().orElse(0);
        double improve = progressList.stream().mapToDouble(p -> p.getImprovementRate() == null ? 0 : p.getImprovementRate()).average().orElse(0);

        return Map.of("avgScore", avg, "improvementRate", improve, "count", progressList.size());
    }
}
