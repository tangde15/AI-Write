package com.write.write.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 全局配置
 * ------------------------
 * 1. 允许 Vue 前端跨域访问后端接口
 * 2. 可配置静态资源映射（如上传作文图片、导出文件）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置跨域（CORS）
     * 允许前端 http://localhost:5173 调用后端接口
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许前端地址（Vue 开发端口）
                .allowedOrigins("http://localhost:5173")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许的请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 是否允许携带 cookie（用于 session 登录）
                .allowCredentials(true)
                // 预检请求有效期（秒）
                .maxAge(3600);
    }

    /**
     * 可选：静态资源映射
     * 如果项目有文件上传功能，可以将 /uploads 映射到本地路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
