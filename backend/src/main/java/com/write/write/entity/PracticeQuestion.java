package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "practice_question")
@Getter
@Setter
public class PracticeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    // OBJECTIVE/ SUBJECTIVE
    @Column(nullable = false)
    private String type;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
