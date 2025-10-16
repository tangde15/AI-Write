# AI五感作文训练平台 - 后端服务

## 项目概述
基于Spring Boot的智能作文教学系统后端服务，支持学生、教师、家长三种角色，提供作文写作、AI批改、进度跟踪、激励互动等功能。

## 主要功能
- ✅ 用户注册、登录、登出
- ✅ 作文提交和AI智能批改
- ✅ 学习进度跟踪和可视化
- ✅ 师生、亲子绑定关系管理
- ✅ 激励语发送和接收
- ✅ 历史记录保存和查询

## 技术栈
- Spring Boot 3.5.5
- Spring Data JPA
- MySQL 8.0
- BCrypt密码加密
- SiliconFlow AI API
- Maven构建工具

## 快速启动
1. 配置MySQL数据库连接（application.yml）
2. 获取SiliconFlow API密钥
3. 运行：mvn spring-boot:run
4. 访问：http://localhost:8080

## 详细文档
请查看 README.md 文件获取完整的部署和配置指南。

## 更新记录
- v1.0.0: 初始版本，包含完整的用户管理和作文功能
- 增加了账号注册，登录和登出功能
- 增加保存历史记录的功能
- 新增主动绑定学生功能
