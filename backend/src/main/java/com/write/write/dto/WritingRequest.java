package com.write.write.dto;

import lombok.Data;

/**
 * 作文写作请求 DTO
 * - topic：作文题目
 * - essay：作文内容
 * - requirement：写作要求
 */
@Data
public class WritingRequest {
    private String topic;
    private String essay;
    private String requirement;
}
