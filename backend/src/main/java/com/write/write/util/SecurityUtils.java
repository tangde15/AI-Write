package com.write.write.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 安全工具类 - 用于获取当前登录用户信息
 */
public class SecurityUtils {
    
    /**
     * 获取当前登录用户的ID
     */
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getSession().getAttribute("userId");
            if (userId != null) {
                return (Long) userId;
            }
        }
        throw new RuntimeException("未登录或会话已过期");
    }
    
    /**
     * 获取当前登录用户的角色
     */
    public static String getCurrentUserRole() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object role = request.getSession().getAttribute("role");
            if (role != null) {
                return (String) role;
            }
        }
        throw new RuntimeException("未登录或会话已过期");
    }
}
