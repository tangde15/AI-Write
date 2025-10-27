package com.write.write.dto;

import lombok.Data;

/**
 * 作文写作请求 DTO
 * - topic：作文题目
 * - essay：作文内容
 * - requirement：写作要求
 * - previousWritingId：要对比的历史作文ID（可选）
 */
@Data
public class WritingRequest {
    private String topic;
    private String essay;
    private String requirement;
    
    /** 如果选择与历史作文对比，这里存储要对比的历史作文ID **/
    private Long previousWritingId;
}
