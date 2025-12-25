package com.write.write.repository;

import com.write.write.entity.PracticeAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PracticeAnswerRepository extends JpaRepository<PracticeAnswer, Long> {
    Optional<PracticeAnswer> findTopByQuestionIdAndUserIdOrderByUpdatedAtDesc(Long questionId, Long userId);
    long countByQuestionIdInAndUserIdAndStatus(Iterable<Long> questionIds, Long userId, String status);
    List<PracticeAnswer> findByQuestionIdInAndUserId(Iterable<Long> questionIds, Long userId);
    List<PracticeAnswer> findByUserIdOrderByUpdatedAtAsc(Long userId);
}
