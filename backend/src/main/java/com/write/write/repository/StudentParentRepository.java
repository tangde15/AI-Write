package com.write.write.repository;

import com.write.write.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
    List<StudentParent> findByParentId(Long parentId);
    List<StudentParent> findByStudentId(Long studentId);
    boolean existsByParentIdAndStudentId(Long parentId, Long studentId);
}
