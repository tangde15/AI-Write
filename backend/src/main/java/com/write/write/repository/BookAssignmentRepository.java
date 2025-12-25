package com.write.write.repository;

import com.write.write.entity.BookAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookAssignmentRepository extends JpaRepository<BookAssignment, Long> {
    List<BookAssignment> findByBookId(Long bookId);
    List<BookAssignment> findByStudentId(Long studentId);
    List<BookAssignment> findByTeacherId(Long teacherId);
    Optional<BookAssignment> findByBookIdAndStudentId(Long bookId, Long studentId);
}
