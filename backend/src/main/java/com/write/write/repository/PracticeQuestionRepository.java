package com.write.write.repository;

import com.write.write.entity.PracticeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeQuestionRepository extends JpaRepository<PracticeQuestion, Long> {
    List<PracticeQuestion> findByBookIdOrderByIdAsc(Long bookId);
    long countByBookId(Long bookId);
}
