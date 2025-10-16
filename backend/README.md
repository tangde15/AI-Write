# AI五感作文训练平台

## 项目简介

AI五感作文训练平台是一个基于Spring Boot + Vue.js的智能作文教学系统，旨在通过AI技术帮助学生提升写作能力。系统支持学生、教师、家长三种角色，提供作文写作、AI批改、进度跟踪、激励互动等功能。

## 功能特性

### 学生端功能
- 📝 智能作文写作：基于五感写作法进行作文创作
- 🤖 AI智能批改：实时获得AI反馈和建议
- 📊 学习进度跟踪：可视化展示写作进步曲线
- 💬 接收激励语：查看来自教师和家长的鼓励
- 🔗 绑定关系管理：主动绑定教师和家长

### 教师端功能
- 👥 学生管理：查看和管理绑定的学生
- 📖 作文批改：对学生作文进行专业点评
- 📈 进度监控：跟踪学生学习进度和成长曲线
- 💌 发送激励语：给学生发送鼓励和指导
- 🔗 主动绑定学生：通过用户名绑定学生

### 家长端功能
- 👶 孩子管理：查看和管理绑定的孩子
- 📊 成长监控：实时了解孩子的学习进度
- 💝 发送激励语：给孩子发送鼓励和关爱
- 📚 作文查看：查看孩子的作文作品和反馈
- 🔗 主动绑定孩子：通过用户名绑定孩子

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.5.5
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **安全**: BCrypt密码加密
- **AI集成**: SiliconFlow API
- **构建工具**: Maven
- **Java版本**: JDK 17

### 前端技术
- **框架**: Vue.js 3.3.4
- **路由**: Vue Router 4.2.4
- **状态管理**: Pinia 2.1.6
- **UI组件**: Element Plus 2.3.14
- **图表**: ECharts 5.4.3
- **HTTP客户端**: Axios 1.5.0
- **构建工具**: Vite 4.4.9

## 环境要求

### 开发环境
- **JDK**: 17或更高版本
- **Node.js**: 16或更高版本
- **MySQL**: 8.0或更高版本
- **Maven**: 3.6或更高版本

### 生产环境
- **服务器**: Linux/Windows Server
- **内存**: 最少2GB RAM
- **存储**: 最少10GB可用空间

## 快速开始

### 1. 数据库配置

#### 创建数据库
```sql
-- 执行 CREATE_DATABASE.sql 文件
CREATE DATABASE IF NOT EXISTS aiwriting 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

#### 创建数据表
```sql
-- 执行 schema.sql 文件中的建表语句
-- 或者让Spring Boot自动创建（推荐开发环境）
```

### 2. 后端配置

#### 修改数据库连接配置
编辑 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aiwriting?useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username    # 修改为你的MySQL用户名
    password: your_password    # 修改为你的MySQL密码
    driver-class-name: com.mysql.cj.jdbc.Driver

ai:
  api:
    url: https://api.siliconflow.cn/v1/chat/completions
    key: your_api_key          # 修改为你的SiliconFlow API密钥
```

#### 启动后端服务
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 3. 前端配置

#### 安装依赖
```bash
cd frontend
npm install
```

#### 启动开发服务器
```bash
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

## 部署指南

### 在其他主机上部署

#### 1. 环境准备
```bash
# 安装JDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# 安装Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 安装MySQL 8.0
sudo apt install mysql-server-8.0
```

#### 2. 数据库配置
```bash
# 启动MySQL服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 创建数据库和用户
sudo mysql -u root -p
```

在MySQL中执行：
```sql
CREATE DATABASE aiwriting CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'aiwriting_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON aiwriting.* TO 'aiwriting_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 3. 应用配置修改

**后端配置** (`backend/src/main/resources/application.yml`):
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aiwriting?useSSL=false&serverTimezone=Asia/Shanghai
    username: aiwriting_user
    password: your_secure_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false  # 生产环境建议关闭
    properties:
      hibernate.format_sql: false
      hibernate.dialect: org.hibernate.dialect.MySQLDialect

ai:
  api:
    url: https://api.siliconflow.cn/v1/chat/completions
    key: your_siliconflow_api_key  # 替换为你的API密钥

logging:
  level:
    root: WARN
    com.write.write: INFO
```

**前端配置** (`frontend/src/api/auth.js`):
```javascript
// 修改API基础URL为你的服务器地址
const API_BASE_URL = 'http://your-server-ip:8080/api'
```

#### 4. 构建和部署

**后端构建**:
```bash
cd backend
mvn clean package -DskipTests
java -jar target/Write-0.0.1-SNAPSHOT.jar
```

**前端构建**:
```bash
cd frontend
npm run build
# 将dist目录部署到Web服务器（如Nginx）
```

#### 5. 使用Nginx部署前端（可选）
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /path/to/frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## API文档

### 认证相关
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出

### 绑定相关
- `POST /api/binding/teacher/bind-student` - 教师绑定学生
- `POST /api/binding/parent/bind-child` - 家长绑定孩子
- `GET /api/binding/my-bindings` - 获取我的绑定列表

### 作文相关
- `POST /api/writing/submit` - 提交作文
- `GET /api/writing/records` - 获取作文记录
- `GET /api/writing/progress` - 获取学习进度

### 激励语相关
- `POST /api/encouragement/send` - 发送激励语
- `GET /api/encouragement/messages` - 获取激励语列表

## 数据库表结构

### 用户表 (user_account)
- `id`: 主键
- `username`: 用户名（唯一）
- `password`: 密码（BCrypt加密）
- `role`: 角色（STUDENT/TEACHER/PARENT）

### 作文记录表 (writing_record)
- `id`: 主键
- `user_id`: 用户ID
- `topic`: 作文题目
- `essay`: 作文内容
- `ai_response`: AI反馈
- `teacher_feedback`: 教师批改
- `created_at`: 创建时间
- `updated_at`: 更新时间

### 学习进度表 (writing_progress)
- `id`: 主键
- `student_id`: 学生ID
- `avg_score`: 平均分数
- `improvement_rate`: 进步率
- `date`: 日期

### 绑定关系表
- `student_teacher`: 师生绑定关系
- `student_parent`: 亲子绑定关系

### 激励语表 (encouragement_message)
- `id`: 主键
- `student_id`: 学生ID
- `sender_id`: 发送者ID
- `from_role`: 发送者角色
- `content`: 激励语内容
- `created_at`: 创建时间

## 常见问题

### Q: 如何获取SiliconFlow API密钥？
A: 访问 [SiliconFlow官网](https://siliconflow.cn) 注册账号并获取API密钥。

### Q: 数据库连接失败怎么办？
A: 检查MySQL服务是否启动，用户名密码是否正确，数据库是否存在。

### Q: 前端无法连接后端？
A: 检查后端服务是否启动，端口是否被占用，CORS配置是否正确。

### Q: 如何重置数据库？
A: 删除数据库后重新执行 `CREATE_DATABASE.sql` 和 `schema.sql`。

## 开发团队

- **后端开发**: Spring Boot + JPA
- **前端开发**: Vue.js + Element Plus
- **数据库设计**: MySQL
- **AI集成**: SiliconFlow API

## 许可证

本项目采用 MIT 许可证，详情请查看 LICENSE 文件。

## 更新日志

### v1.0.0 (2024-01-XX)
- ✨ 初始版本发布
- 🎯 支持学生、教师、家长三种角色
- 🤖 集成AI作文批改功能
- 📊 实现学习进度跟踪
- 💬 支持激励语互动
- 🔗 实现主动绑定功能

---

如有问题，请提交Issue或联系开发团队。


