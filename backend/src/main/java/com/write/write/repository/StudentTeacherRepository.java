package com.write.write.repository;

import com.write.write.entity.StudentTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentTeacherRepository extends JpaRepository<StudentTeacher, Long> {
    List<StudentTeacher> findByTeacherId(Long teacherId);
    List<StudentTeacher> findByStudentId(Long studentId);
    boolean existsByTeacherIdAndStudentId(Long teacherId, Long studentId);
}
