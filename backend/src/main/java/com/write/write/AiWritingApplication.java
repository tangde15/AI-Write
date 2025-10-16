package com.write.write;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目主启动类
 * ----------------------------
 * AI 五感作文训练平台
 * 后端技术栈：Spring Boot + JPA + MySQL
 * 前端技术栈：Vue3 + Vite + Element Plus
 * ----------------------------
 * 功能模块：
 *  - 学生端：作文提交 / AI反馈 / 激励语展示
 *  - 教师端：学生作文查看 / 人工批改 / 写作进步统计
 *  - 家长端：查看孩子成长曲线 / 激励语发送与查看
 */
@SpringBootApplication
public class AiWritingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiWritingApplication.class, args);
        System.out.println("🚀 AI 五感作文训练平台后端已启动成功！");
        System.out.println("🌐 API 地址：http://localhost:8080");
    }
}

