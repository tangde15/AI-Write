package com.write.write.service;

import com.write.write.dto.WritingRequest;
import com.write.write.entity.EncouragementMessage;
import com.write.write.entity.UserAccount;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.repository.EncouragementRepository;
import com.write.write.repository.UserRepository;
import com.write.write.repository.WritingProgressRepository;
import com.write.write.repository.WritingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final WritingService writingService;
    private final UserRepository userRepository;
    private final WritingRecordRepository recordRepository;
    private final WritingProgressRepository progressRepository;
    private final EncouragementRepository encouragementRepository;

    /** 学生提交作文 **/
    @Transactional
    public Map<String, String> submitEssay(Long studentId, WritingRequest req) {
        UserAccount student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        String aiFeedback = writingService.handleRequest(req);
        Integer score = writingService.extractScore(aiFeedback);

        WritingRecord record = new WritingRecord();
        record.setUser(student);
        record.setTopic(req.getTopic());
        record.setEssay(req.getEssay());
        record.setAiResponse(aiFeedback);
        record.setScore(score);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        recordRepository.save(record);

        // 如果AI返回了评分，记录写作进步
        if (score != null) {
            updateProgress(studentId, score);
        }

        return Map.of("aiResponse", aiFeedback);
    }
    
    /**
     * 更新学生写作进度（基于真实评分）
     */
    private void updateProgress(Long studentId, Integer currentScore) {
        try {
            // 获取历史进度记录
            List<WritingProgress> historyProgress = progressRepository.findByStudentIdOrderByDateAsc(studentId);
            
            // 计算提升率
            Float improvementRate = 0.0f;
            if (!historyProgress.isEmpty()) {
                WritingProgress lastProgress = historyProgress.get(historyProgress.size() - 1);
                Float lastScore = lastProgress.getAvgScore();
                if (lastScore != null && lastScore > 0) {
                    improvementRate = ((currentScore - lastScore) / lastScore) * 100;
                }
            }
            
            // 创建新的进度记录
            WritingProgress progress = new WritingProgress();
            progress.setStudentId(studentId);
            progress.setAvgScore(currentScore.floatValue());
            progress.setImprovementRate(improvementRate);
            progress.setDate(LocalDateTime.now());
            
            progressRepository.save(progress);
            System.out.println("✅ 进度记录已保存：评分=" + currentScore + "分, 提升率=" + String.format("%.2f", improvementRate) + "%");
        } catch (Exception e) {
            System.err.println("❌ 保存进度记录失败: " + e.getMessage());
        }
    }

    /** 查看历史记录 **/
    public List<WritingRecord> getWritingHistory(Long studentId) {
        UserAccount student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        return recordRepository.findByUserOrderByCreatedAtDesc(student);
    }

    /** 查看写作进步 **/
    public List<WritingProgress> getProgress(Long studentId) {
        return progressRepository.findByStudentIdOrderByDateAsc(studentId);
    }
    
    /** 获取激励语列表 **/
    public List<EncouragementMessage> getEncouragements(Long studentId) {
        return encouragementRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }
}
