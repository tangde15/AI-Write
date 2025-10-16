package com.write.write.service;

import com.write.write.entity.EncouragementMessage;
import com.write.write.entity.UserAccount;
import com.write.write.repository.EncouragementRepository;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EncouragementService {

    private final EncouragementRepository encouragementRepository;
    private final UserRepository userRepository;
    private final WritingService writingService;

    /** AI 生成激励语 **/
    public EncouragementMessage generateAIMessage(Long studentId, String essayContent) {
        UserAccount student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        String prompt = "请根据以下学生作文生成一句积极、鼓励性的短评：\n" + essayContent;
        String aiText = writingService.callAI(prompt);

        EncouragementMessage msg = new EncouragementMessage();
        msg.setStudent(student);
        msg.setContent(aiText);
        msg.setFromRole("AI");
        msg.setCreatedAt(LocalDateTime.now());
        encouragementRepository.save(msg);
        return msg;
    }

    /** 教师或家长手动发送激励语 **/
    public EncouragementMessage addManualMessage(Long senderId, Long studentId, String role, String content) {
        UserAccount sender = userRepository.findById(senderId).orElseThrow();
        UserAccount student = userRepository.findById(studentId).orElseThrow();

        EncouragementMessage msg = new EncouragementMessage();
        msg.setStudent(student);
        msg.setSender(sender);
        msg.setContent(content);
        msg.setFromRole(role);
        msg.setCreatedAt(LocalDateTime.now());
        encouragementRepository.save(msg);
        return msg;
    }

    /** 获取学生所有激励语 **/
    public List<EncouragementMessage> getMessages(Long studentId) {
        return encouragementRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }
}
