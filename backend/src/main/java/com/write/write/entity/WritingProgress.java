package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class WritingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    /** 平均得分（AI 评分或教师评分） **/
    private Float avgScore;

    /** 提升率（相较上一次） **/
    private Float improvementRate;

    private LocalDateTime date;
}
