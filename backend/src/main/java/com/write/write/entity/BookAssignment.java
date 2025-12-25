package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "book_assignment", uniqueConstraints = {
        @UniqueConstraint(name = "uk_book_student", columnNames = {"book_id", "student_id"})
})
public class BookAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "pushed_at", nullable = false)
    private LocalDateTime pushedAt;

    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING | IN_PROGRESS | COMPLETED

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
