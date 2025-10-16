package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class WritingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 所属学生 **/
    @ManyToOne
    private UserAccount user;

    private String topic;

    @Column(columnDefinition = "TEXT")
    private String essay;

    @Column(columnDefinition = "TEXT")
    private String aiResponse;

    @Column(columnDefinition = "TEXT")
    private String teacherFeedback;

    /** AI评分（1-100分） **/
    private Integer score;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
