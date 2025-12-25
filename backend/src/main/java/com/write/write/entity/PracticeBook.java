package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "practice_book")
@Getter
@Setter
public class PracticeBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "library_id")
    private Long libraryId;

    @Column(name = "set_id")
    private Long setId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "grade_tag")
    private String gradeTag;

    @Column(name = "topic_tags")
    private String topicTags;

    @Column(name = "status")
    private String status; // DRAFT | PUBLISHED | CLOSED

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
