package com.write.write.dto;

import lombok.Data;

/**
 * 范文DTO - 用于前端展示
 */
@Data
public class SampleEssayDTO {
    private Long id;
    private String title;
    private String authorName;
    private String author;
    private String content;
    private String tag;
    private Integer favoriteCount;
    private String createdAt;
}


