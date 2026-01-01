# AI五感作文训练平台

<div align="center">

![AI Writing Platform](https://img.shields.io/badge/AI-Writing%20Platform-blue?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=for-the-badge)
![FastAPI](https://img.shields.io/badge/FastAPI-0.121.1-009688?style=for-the-badge)
![Milvus](https://img.shields.io/badge/Milvus-2.6.6-00B4B6?style=for-the-badge)

**基于 AI + 向量检索的智能作文教学系统（微服务架构）**

[功能特性](#功能特性) • [快速开始](#快速开始) • [部署指南](#部署指南) • [API文档](#api文档)

</div>

## 📖 项目简介

AI五感作文训练平台是一个创新的智能作文教学系统，采用**微服务架构**集成向量检索（Milvus）+ LLM（DeepSeek）技术，提供上下文感知的作文批改。系统支持学生与教师两种角色，形成完整的教学闭环。

### 🎯 核心价值
- **RAG 批改**: 向量检索相似范文，结合 LLM 生成上下文感知的批改建议（含参考标注）
- **五感训练**: 重点关注视觉、听觉、味觉、嗅觉、触觉的细节描写
- **多角色协作**: 学生与教师协同，形成闭环教学
- **数据驱动**: 通过学习进度跟踪，科学评估学生成长

## ✨ 功能特性

### 👨‍🎓 学生端
- 📝 **智能写作**: 基于五感写作法进行作文创作
- 🤖 **RAG 智能批改**: 
  - ✅ 向量检索相似范文（Milvus，top-3）
  - ✅ 基于范文进行上下文感知批改（DeepSeek LLM）
  - ✅ 标注参考来源（【参考1】【参考2】【参考3】）
  - ✅ 四维评分 + 三部分改进建议（问题→方向→做法）
- 📊 **进度跟踪**: 可视化展示学习进步曲线
- 🔄 **作文对比**: 与历史作文对比分析，AI从五感角度指出进步亮点
- 💬 **接收激励**: 查看来自教师与系统的鼓励
- 🔗 **关系管理**: 主动绑定教师

### 👨‍🏫 教师端
- 👥 **学生管理**: 查看和管理绑定的学生
- 📖 **专业批改**: 对学生作文进行深度点评
- 📈 **进度监控**: 跟踪学生学习进度和成长轨迹
- 💌 **激励互动**: 发送个性化鼓励和指导
- 🔗 **主动绑定**: 通过用户名快速绑定学生


## 🚀 快速开始

### 环境要求
| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 位于 conda-env\Library\lib\jvm |
| Python | 3.13 | 隔离虚拟环境 (python-rag-env) |
| Node.js | 20+ | 内置于 conda-env |
| MySQL | 8.0+ | 数据库服务 |
| Milvus | 2.6.6 | Docker 容器（向量数据库）|
| Docker | 最新 | Milvus 运行环境 |

### 1️⃣ 初始化数据库
```bash
mysql -u root -p < CREATE_DATABASE.sql
mysql -u root -p aiwriting < insert-sample-essays.sql  # 可选：导入样本范文
```

### 2️⃣ 启动 Milvus（Docker）
```bash
cd E:\Project Practice\Write
docker-compose up -d
curl http://localhost:19530/healthz  # 验证连接
```

### 3️⃣ 初始化 Milvus 集合与导入范文
```powershell
cd "E:\Project Practice\Write\rag-service"
& "E:\Project Practice\Write\python-rag-env\Scripts\python.exe" init_milvus_collection.py
& "E:\Project Practice\Write\python-rag-env\Scripts\python.exe" import_sample_essays.py
```

### 4️⃣ 配置 RAG 服务
编辑 `rag-service/config.env`:
```env
SILICONFLO_API_KEY=your_api_key
DEEPSEEK_API_KEY=your_api_key
MILVUS_HOST=127.0.0.1
MILVUS_PORT=19530
MILVUS_DATABASE=Write
```

### 5️⃣ 启动三个服务

**终端 1 - Spring Boot 后端（端口 8080）**
```powershell
cd "E:\Project Practice\Write\backend"
$env:JAVA_HOME="E:\Project Practice\Write\conda-env\Library\lib\jvm"
.\mvnw.cmd clean compile spring-boot:run -DskipTests
```

**终端 2 - Python RAG 微服务（端口 8001）**
```powershell
cd "E:\Project Practice\Write\rag-service"
& "E:\Project Practice\Write\python-rag-env\Scripts\python.exe" -m uvicorn rag_service:app --host 0.0.0.0 --port 8001
```

**终端 3 - React 前端（端口 5173）**
```powershell
cd "E:\Project Practice\Write\frontend"
npm run dev -- --host 0.0.0.0
```

### 6️⃣ 访问系统
| 服务 | URL |
|------|-----|
| 前端 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| RAG 服务 | http://localhost:8001/health |
| Milvus | http://localhost:19530 |

### 7️⃣ 一键启动/停止（可选）
```powershell
& "E:\Project Practice\Write\scripts\start-full-stack.ps1" -EnvPrefix "E:\Project Practice\Write\conda-env"
& "E:\Project Practice\Write\scripts\stop-full-stack.ps1"
```

---

## 🏛️ 系统架构概览

```
学生提交作文
    ↓
Spring Boot WritingService
    ↓
[RAG 可用？] ──是──> RagClient (HTTP POST)
    ├─[否]──> 降级到传统 AI 批改
    ↓
Python FastAPI RAG 服务（端口 8001）
  ├─ 向量化作文（SiliconFlow API, dim=1024）
  ├─ Milvus 向量检索（top-3 相似范文, COSINE 相似度）
  ├─ 格式化参考材料
  ├─ 填充提示词模板
  └─ 调用 DeepSeek LLM
    ↓
批改反馈（含【参考1】【参考2】【参考3】）
    ↓
返回 WritingService → 解析评分 → 保存数据库
```

---

## 📊 批改输出格式

```
评分：XX分

**总体评价**
作文整体评价（80字以内）

**内容评分：XX/30分**
主题立意、材料选择、充实程度评价（100字以内）

**结构评分：XX/20分**
结构、层次、衔接评价（80字以内）

**语言评分：XX/30分**
语言表达效果评价（100字以内）

**创意评分：XX/20分**
立意、选材、表达新颖性评价（80字以内）

**修改建议**
【问题】明确指出具体不足
【方向】说明改进的大方向  
【做法】给出具体可操作的方法和示例

【问题】...
【方向】...
【做法】...
```

---

## 🔧 故障排查

| 问题 | 解决方案 |
|------|--------|
| Milvus 连接失败 | 确认 Docker 运行：`docker ps \| grep milvus` |
| RAG 返回 422 | 检查字段名为 snake_case（essay_text 非 essayText） |
| Java 环境错误 | 检查 .vscode/settings.json 的 java.home 路径需包含 /lib/jvm 后缀 |
| Maven 写入 C 盘 | 配置 backend/.mvn/maven.config 的本地仓库路径 |
| 代码改动未生效 | 执行 `mvn clean compile` 清理编译文件，重启 Spring Boot |

---

## 🏗️ 项目结构

# AI 五感作文训练平台

<div align="center">

![AI Writing Platform](https://img.shields.io/badge/AI-Writing%20Platform-blue?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=for-the-badge)
![Vue.js](https://img.shields.io/badge/Vue.js-3.3.4-4FC08D?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange?style=for-the-badge)

**基于 AI 的智能作文教学系统：学生/教师双端协同，支持 AI 批改、题单推送、实时私信与学习进度跟踪。**

[功能特性](#功能特性) • [架构与技术栈](#架构与技术栈) • [本地运行](#本地运行) • [可观测性](#可观测性) • [API 概览](#api-概览) • [部署指南](#部署指南) • [常见问题](#常见问题)

</div>

---

## 项目简介
本系统通过“五感写作法”训练学生作文能力，结合 AI 进行批改与建议，教师可推送题单与点评，形成闭环教学。

### 核心价值
- 智能批改：AI 提供个性化反馈与改进建议。
- 教学闭环：学生-教师双角色协同。
- 数据驱动：进度追踪与可视化评估。
- 实时互动：内置私信与通知功能。

---

## 功能特性
### 学生端
- 智能写作与 AI 批改、进度曲线、历史作文对比、接收激励与关系绑定。

### 教师端
- 学生管理、人工批改与进度监控、题单创建/推送、激励互动。

<!-- 家长端暂不提供，以下内容已移除 -->

---

## 架构与技术栈
### 后端
- 框架：Spring Boot 3.5.5
- 数据库：MySQL 8.0+
- ORM：Spring Data JPA + Hibernate
- 实时通信：WebSocket（STOMP）
- 可观测性：请求日志过滤器、关联 ID（traceId）、统一异常响应
- 构建：Maven；异步：`@EnableAsync` + `@Async`

### 前端
- 框架：React 18 + Vite
- 样式：Tailwind CSS
- HTTP：Axios
- 构建：Vite

### 微服务（RAG）
- 框架：FastAPI 0.121.1（异步 Web 框架）
- 向量检索：Pymilvus 2.6.2（Milvus Python 客户端）
- 服务器：Uvicorn 0.38.0（ASGI 服务器）
- 语言：Python 3.13

### 数据与 AI
- Milvus 2.6.6 - 向量数据库（Docker）
- DeepSeek API - LLM 服务
- SiliconFlow API - 向量化服务（BAAI/bge-m3, dim=1024）

---

## 本地运行
### 前置要求
- JDK 17+、Node.js 16+、MySQL 8+、Maven 3.6+

### 1. 克隆代码
```bash
git clone <repository-url>
cd ai-writing-platform
```

### 2. 数据库初始化
```bash
# 创建数据库（脚本在仓库根目录）
mysql -u root -p < CREATE_DATABASE.sql

# 导入表结构
mysql -u root -p aiwriting < backend/src/main/resources/schema.sql
```

### 3. 后端配置与启动
编辑 `backend/src/main/resources/application.yml`，填入数据库与 AI API Key（请勿提交真实密钥）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aiwriting?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: <your_password>
    driver-class-name: com.mysql.cj.jdbc.Driver

ai:
  api:
    url: https://api.siliconflow.cn/v1/chat/completions
    key: <your_siliconflow_api_key>
```
启动后端：
```bash
cd backend
mvn spring-boot:run
```

### 4. 前端配置与启动
```bash
cd frontend
npm install
# 必须在 frontend 目录下执行
npm run dev -- --host 0.0.0.0
```

### 5. 访问地址
- 前端：http://localhost:5173
- 后端 API：http://localhost:8080

### 6. 一键启动/停止（推荐）
使用已提供的 PowerShell 脚本在 Conda 前缀环境中一键启动前后端：
```powershell
& "E:\Project Practice\Write\scripts\start-full-stack.ps1" -EnvPrefix "E:\Project Practice\Write\conda-env"
& "E:\Project Practice\Write\scripts\stop-full-stack.ps1"
```
脚本位置： [scripts/start-full-stack.ps1](scripts/start-full-stack.ps1) 、[scripts/stop-full-stack.ps1](scripts/stop-full-stack.ps1)

---

## 可观测性
- 访问日志：记录 方法/URL/状态码/耗时，并输出 `traceId`。
- 关联 ID：请求头 `X-Request-Id` 会被透传或自动生成，并在响应头回传；日志包含同一 `traceId`。
- 统一错误响应：异常返回统一结构，示例：
```json
{
  "success": false,
  "error": "INTERNAL_ERROR",
  "message": "服务器开小差了，请稍后再试",
  "traceId": "a1b2c3d4..."
}
```
实现文件：
- [backend/src/main/java/com/write/write/config/CorrelationIdFilter.java](backend/src/main/java/com/write/write/config/CorrelationIdFilter.java)
- [backend/src/main/java/com/write/write/config/RequestLoggingFilter.java](backend/src/main/java/com/write/write/config/RequestLoggingFilter.java)
- [backend/src/main/java/com/write/write/config/GlobalExceptionHandler.java](backend/src/main/java/com/write/write/config/GlobalExceptionHandler.java)
日志格式：参见 [backend/src/main/resources/application.yml](backend/src/main/resources/application.yml) 的 `logging.pattern.console`。

---

## API 概览
### 认证
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/logout` | 用户登出 |

### 绑定关系
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/binding/teacher/bind-student` | 教师绑定学生 |
<!-- 家长绑定接口暂不提供，以下条目已移除 -->
| GET | `/api/binding/my-bindings` | 获取绑定列表 |

### 作文与进度
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/writing/submit` | 提交作文 |
| GET | `/api/writing/records` | 获取作文记录 |
| GET | `/api/writing/progress` | 获取学习进度 |

更多接口详见后端代码与前端 API 模块。

---

## 部署指南
### 后端
```bash
cd backend
mvn clean package -DskipTests
java -jar target/Write-0.0.1-SNAPSHOT.jar
```

### 前端
```bash
cd frontend
npm run build
# 将 dist 目录部署到 Web 服务器
```

---

## 常见问题
- ENOENT: `Could not read package.json`
  - 原因：在项目根目录执行了 `npm run dev`，而 `package.json` 位于 `frontend/`。
  - 解决：进入前端目录后再执行：
```powershell
Set-Location "E:\Project Practice\Write\frontend"
npm install
npm run dev -- --host 0.0.0.0
```

---

## 贡献与许可
- 贡献流程：Fork → 分支 → 提交 → PR。
- 许可证：MIT（见 LICENSE）。

<div align="center">

**⭐ 如果这个项目对你有帮助，请给它一个星标！**

Made with ❤️ by AI Writing Platform Team

</div>

---

## Postman 示例：统一错误响应截图（含 traceId）

目标：演示 `@RestControllerAdvice` 的统一错误响应，响应体包含 `traceId`，便于定位。

步骤一：准备
- 启动后端（确保无启动报错）。
- 新建请求：方法 GET，URL `http://localhost:8080/api/dev/boom`（此接口用于演示，服务端会主动抛出异常）。
- Headers 添加（可选但推荐）：`X-Request-Id: demo-500`。

步骤二：发送并截图
- 点击发送，预期返回 `500 Internal Server Error`。
- 响应 JSON 示例：
  ```json
  {
    "success": false,
    "data": null,
    "error": "INTERNAL_ERROR",
    "message": "服务器开小差了，请稍后再试",
    "traceId": "demo-500" // 或系统自动生成的 UUID
  }
  ```
- 在 Postman 画面中包含：请求栏、Headers（显示 `X-Request-Id`）、响应区（状态码 + JSON）。

可选补充：404 统一错误截图
- 访问不存在的地址：`http://localhost:8080/api/dev/not-exist`。
- 预期返回 `404 Not Found`，响应结构统一为 `error: NOT_FOUND`，同样包含 `traceId`。

```






























