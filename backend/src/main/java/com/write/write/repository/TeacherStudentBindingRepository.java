package com.write.write.repository;

import com.write.write.entity.TeacherStudentBinding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherStudentBindingRepository extends JpaRepository<TeacherStudentBinding, Long> {
    List<TeacherStudentBinding> findByStudentId(Long studentId);
    List<TeacherStudentBinding> findByTeacherId(Long teacherId);
    Optional<TeacherStudentBinding> findByTeacherIdAndStudentId(Long teacherId, Long studentId);
    void deleteByTeacherIdAndStudentId(Long teacherId, Long studentId);
}
