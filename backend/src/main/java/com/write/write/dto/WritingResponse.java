package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 回复结果 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingResponse {
    private String result;  // 前端期望的字段名
}
