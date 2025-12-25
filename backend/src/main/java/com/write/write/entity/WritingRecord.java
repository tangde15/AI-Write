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

    /** 如果这篇作文是与之前作文对比后提交的，这里存储被对比作文的ID **/
    private Long previousRecordId;

    /** AI对比分析结果（TEXT类型存储详细的对比分析） **/
    @Column(columnDefinition = "TEXT")
    private String comparisonAnalysis;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 教师重批维度分（可选） **/
    private Integer teacherContentScore;
    private Integer teacherStructureScore;
    private Integer teacherLanguageScore;
    private Integer teacherCreativityScore;
    private Integer teacherOverallScore;
    private LocalDateTime teacherReviewedAt;
}
