package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teacherId;
    private Long studentId;
}
