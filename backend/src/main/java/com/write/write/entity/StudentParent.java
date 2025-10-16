package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;
    private Long studentId;
}
