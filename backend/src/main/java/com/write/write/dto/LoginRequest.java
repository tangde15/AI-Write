package com.write.write.dto;

import lombok.Data;

/**
 * 登录请求 DTO
 */
@Data
public class LoginRequest {
    private String account;
    private String password;
    private String role; // 用户选择的角色（用于验证）
}
