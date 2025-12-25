package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "practice_set")
@Getter
@Setter
public class PracticeSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
