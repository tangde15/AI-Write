package com.write.write.controller;

import com.write.write.dto.WritingRequest;
import com.write.write.dto.WritingResponse;
import com.write.write.entity.WritingProgress;
import com.write.write.entity.WritingRecord;
import com.write.write.entity.UserAccount;
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
        
        String result;
        String comparisonAnalysis = null;
        Integer previousScore = null;
        
        // 判断是否为对比模式
        if (request.getPreviousWritingId() != null && request.getPreviousWritingId() > 0) {
            // 获取要对比的历史作文
            WritingRecord previousRecord = writingRecordRepository.findById(request.getPreviousWritingId())
                    .orElse(null);
            
            if (previousRecord != null && previousRecord.getUser().getId().equals(userId)) {
                // 调用对比分析AI
                result = writingService.handleComparisonRequest(request, previousRecord);
                comparisonAnalysis = result;
                previousScore = previousRecord.getScore();
                
                System.out.println("📊 对比分析模式：将新作文与ID " + request.getPreviousWritingId() + " 进行对比");
            } else {
                // 如果找不到历史作文，使用普通模式
                result = writingService.handleRequest(request);
                System.out.println("⚠️ 找不到历史作文或无权访问，使用普通模式");
            }
        } else {
            // 普通模式
            result = writingService.handleRequest(request);
        }
        
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
            
            // 如果是对比模式，保存对比分析信息
            if (comparisonAnalysis != null && request.getPreviousWritingId() != null) {
                record.setPreviousRecordId(request.getPreviousWritingId());
                record.setComparisonAnalysis(comparisonAnalysis);
            }
            
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

    @GetMapping("/list")
    public ResponseEntity<List<WritingRecord>> list(@RequestParam(required = false) Long studentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // 如果带 studentId，允许教师查看学生记录（简化权限）
        if (studentId != null) {
            UserAccount student = userRepository.findById(studentId).orElse(null);
            if (student == null) return ResponseEntity.notFound().build();
            List<WritingRecord> list = writingRecordRepository.findByUserOrderByCreatedAtDesc(student);
            return ResponseEntity.ok(list);
        }

        // 返回当前用户的写作记录
        UserAccount user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<WritingRecord> list = writingRecordRepository.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<WritingRecord> detail(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        WritingRecord r = writingRecordRepository.findById(id).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
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
