package com.write.write.dto;

import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // STUDENT / TEACHER / PARENT
}
