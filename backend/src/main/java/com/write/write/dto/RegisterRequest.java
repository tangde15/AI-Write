package com.write.write.dto;

import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterRequest {
    private String username;
    private String account;
    private String password;
    private String role; // STUDENT / TEACHER / PARENT
    private String educationLevel; // PRIMARY / MIDDLE
    private String grade; // GRADE_1 ~ GRADE_6 (小学) / GRADE_1 ~ GRADE_3 (初中)
}
