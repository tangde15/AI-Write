package com.write.write.repository;

import com.write.write.entity.EncouragementMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EncouragementRepository extends JpaRepository<EncouragementMessage, Long> {
    List<EncouragementMessage> findByStudentIdOrderByCreatedAtDesc(Long studentId);
}
