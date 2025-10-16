package com.write.write.controller;

import com.write.write.dto.WritingRequest;
import com.write.write.dto.WritingResponse;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.service.WritingService;
import com.write.write.repository.WritingProgressRepository;
import com.write.write.repository.WritingRecordRepository;
import com.write.write.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingController {

    private final WritingService writingService;
    private final WritingRecordRepository writingRecordRepository;
    private final UserRepository userRepository;
    private final WritingProgressRepository progressRepository;

    @PostMapping("/process")
    public ResponseEntity<WritingResponse> process(@RequestBody WritingRequest request, HttpSession session) {
        // 获取当前用户ID
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 调用AI获取反馈
        String result = writingService.handleRequest(request);
        
        // 从AI响应中提取评分
        Integer score = writingService.extractScore(result);
        
        // 保存作文记录到数据库
        try {
            WritingRecord record = new WritingRecord();
            record.setUser(userRepository.findById(userId).orElse(null));
            record.setTopic(request.getTopic());
            record.setEssay(request.getEssay());
            record.setAiResponse(result);
            record.setScore(score);
            record.setCreatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());
            
            writingRecordRepository.save(record);
            System.out.println("✅ 作文记录已保存到数据库，ID: " + record.getId() + ", 评分: " + score);
            
            // 如果有评分，计算并保存进度记录
            if (score != null) {
                updateProgress(userId, score);
            }
        } catch (Exception e) {
            System.err.println("❌ 保存作文记录失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(new WritingResponse(result));
    }
    
    /**
     * 更新学生写作进度
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
            e.printStackTrace();
        }
    }
}
