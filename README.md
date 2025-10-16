# AI五感作文训练平台

<div align="center">

![AI Writing Platform](https://img.shields.io/badge/AI-Writing%20Platform-blue?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green?style=for-the-badge)
![Vue.js](https://img.shields.io/badge/Vue.js-3.3.4-4FC08D?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange?style=for-the-badge)

**基于AI技术的智能作文教学系统**

[功能特性](#功能特性) • [快速开始](#快速开始) • [部署指南](#部署指南) • [API文档](#api文档)

</div>

## 📖 项目简介

AI五感作文训练平台是一个创新的智能作文教学系统，通过AI技术帮助学生提升写作能力。系统采用前后端分离架构，支持学生、教师、家长三种角色，提供完整的作文教学生态。

### 🎯 核心价值
- **智能化教学**: 利用AI技术提供个性化作文批改和指导
- **多角色协作**: 学生、教师、家长三方协同，形成完整教学闭环
- **数据驱动**: 通过学习进度跟踪，科学评估学生成长
- **情感互动**: 激励语系统增强学习动力和家庭参与

## ✨ 功能特性

### 👨‍🎓 学生端
- 📝 **智能写作**: 基于五感写作法进行作文创作
- 🤖 **AI批改**: 实时获得AI反馈和改进建议
- 📊 **进度跟踪**: 可视化展示学习进步曲线
- 💬 **接收激励**: 查看来自教师和家长的鼓励
- 🔗 **关系管理**: 主动绑定教师和家长

### 👨‍🏫 教师端
- 👥 **学生管理**: 查看和管理绑定的学生
- 📖 **专业批改**: 对学生作文进行深度点评
- 📈 **进度监控**: 跟踪学生学习进度和成长轨迹
- 💌 **激励互动**: 发送个性化鼓励和指导
- 🔗 **主动绑定**: 通过用户名快速绑定学生

### 👨‍👩‍👧‍👦 家长端
- 👶 **孩子管理**: 查看和管理绑定的孩子
- 📊 **成长监控**: 实时了解孩子的学习状态
- 💝 **情感支持**: 发送关爱和鼓励给孩子
- 📚 **作品查看**: 查看孩子的作文作品和反馈
- 🔗 **主动绑定**: 通过用户名快速绑定孩子

## 🚀 快速开始

### 环境要求
- **JDK**: 17+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Maven**: 3.6+

### 1️⃣ 克隆项目
```bash
git clone <repository-url>
cd ai-writing-platform
```

### 2️⃣ 数据库配置
```bash
# 创建数据库
mysql -u root -p < CREATE_DATABASE.sql

# 导入表结构
mysql -u root -p aiwriting < backend/src/main/resources/schema.sql
```

### 3️⃣ 后端配置
```bash
cd backend

# 修改数据库配置
vim src/main/resources/application.yml
# 更新数据库连接信息和SiliconFlow API密钥

# 启动后端服务
mvn spring-boot:run
```

### 4️⃣ 前端配置
```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 5️⃣ 访问系统
- 前端地址: http://localhost:5173
- 后端API: http://localhost:8080

## 🏗️ 项目结构

```
ai-writing-platform/
├── backend/                 # Spring Boot后端
│   ├── src/main/java/      # Java源码
│   ├── src/main/resources/ # 配置文件和SQL脚本
│   └── pom.xml            # Maven配置
├── frontend/               # Vue.js前端
│   ├── src/               # Vue源码
│   ├── public/            # 静态资源
│   └── package.json       # NPM配置
├── CREATE_DATABASE.sql    # 数据库创建脚本
└── README.md             # 项目说明
```

## 🔧 部署指南

### 在其他主机上部署

#### 1. 环境准备
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk nodejs npm mysql-server-8.0

# CentOS/RHEL
sudo yum install java-17-openjdk nodejs npm mysql-server
```

#### 2. 数据库配置
```bash
# 启动MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 创建数据库和用户
sudo mysql -u root -p
```

```sql
CREATE DATABASE aiwriting CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'aiwriting_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON aiwriting.* TO 'aiwriting_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 3. 应用配置

**后端配置** (`backend/src/main/resources/application.yml`):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aiwriting?useSSL=false&serverTimezone=Asia/Shanghai
    username: aiwriting_user
    password: your_secure_password
    driver-class-name: com.mysql.cj.jdbc.Driver

ai:
  api:
    url: https://api.siliconflow.cn/v1/chat/completions
    key: your_siliconflow_api_key
```

**前端配置** (`frontend/src/api/auth.js`):
```javascript
const API_BASE_URL = 'http://your-server-ip:8080/api'
```

#### 4. 构建部署
```bash
# 后端构建
cd backend
mvn clean package -DskipTests
java -jar target/Write-0.0.1-SNAPSHOT.jar

# 前端构建
cd frontend
npm run build
# 将dist目录部署到Web服务器
```

## 📚 API文档

### 认证接口
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/logout` | 用户登出 |

### 绑定接口
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/binding/teacher/bind-student` | 教师绑定学生 |
| POST | `/api/binding/parent/bind-child` | 家长绑定孩子 |
| GET | `/api/binding/my-bindings` | 获取绑定列表 |

### 作文接口
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/writing/submit` | 提交作文 |
| GET | `/api/writing/records` | 获取作文记录 |
| GET | `/api/writing/progress` | 获取学习进度 |

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 3.5.5
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **安全**: BCrypt密码加密
- **AI集成**: SiliconFlow API
- **构建**: Maven

### 前端技术
- **框架**: Vue.js 3.3.4
- **路由**: Vue Router 4.2.4
- **状态管理**: Pinia 2.1.6
- **UI组件**: Element Plus 2.3.14
- **图表**: ECharts 5.4.3
- **HTTP**: Axios 1.5.0
- **构建**: Vite 4.4.9

## 🔐 安全说明

- 密码使用BCrypt加密存储
- 支持跨域请求配置
- 会话管理确保用户状态安全
- API接口需要登录验证

## 📝 更新日志

### v1.0.0 (2024-01-XX)
- ✨ 初始版本发布
- 🎯 支持学生、教师、家长三种角色
- 🤖 集成AI作文批改功能
- 📊 实现学习进度跟踪
- 💬 支持激励语互动
- 🔗 实现主动绑定功能

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系我们

- 项目链接: [https://github.com/your-username/ai-writing-platform](https://github.com/your-username/ai-writing-platform)
- 问题反馈: [Issues](https://github.com/your-username/ai-writing-platform/issues)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给它一个星标！**

Made with ❤️ by AI Writing Platform Team

</div>


