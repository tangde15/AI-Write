package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 范文实体类
 * 用于存储示例作文，供学生参考学习
 */
@Entity
@Table(name = "sample_essay")
@Data
public class SampleEssay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 标题 **/
    @Column(nullable = false)
    private String title;

    /** 作者姓名 **/
    @Column(nullable = false)
    private String authorName;

    /** 作者信息（如：张三 三年级，李四 初一） **/
    @Column(nullable = false)
    private String author;

    /** 正文内容 **/
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** 标签（如：写人、写景、叙事等） **/
    @Column
    private String tag;

    /** 收藏数 **/
    @Column
    private Integer favoriteCount = 0;

    /** 创建时间 **/
    @Column
    private LocalDateTime createdAt;

    /** 更新时间 **/
    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (favoriteCount == null) {
            favoriteCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


