package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_teacher_student", columnNames = {"teacherId", "studentId"})
    }
)
@Data
public class TeacherStudentBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teacherId;

    // 允许同一学生绑定多名教师（不再对 studentId 设唯一）
    @Column(nullable = false)
    private Long studentId;

    @Column
    private LocalDateTime createdAt;
}
