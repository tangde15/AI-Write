package com.write.write.repository;

import com.write.write.entity.WritingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WritingProgressRepository extends JpaRepository<WritingProgress, Long> {
    List<WritingProgress> findByStudentIdOrderByDateAsc(Long studentId);
}
