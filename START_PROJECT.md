# 🚀 AI五感作文训练平台 - 完整启动指南

## 📋 环境要求

### 必须安装
- ☑️ Java 17+ 
- ☑️ Maven 3.6+
- ☑️ Node.js 16+
- ☑️ MySQL 8.0+

### 检查环境

```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version

# 检查 Node.js 版本
node -version

# 检查 npm 版本
npm -version
```

---

## 🗄️ 第一步：配置数据库

### 1. 启动 MySQL

```bash
# Windows (管理员权限)
net start MySQL

# macOS/Linux
sudo systemctl start mysql
```

### 2. 创建数据库

打开 MySQL 命令行或 MySQL Workbench：

```sql
-- 登录 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE ai_writing_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 查看数据库
SHOW DATABASES;

-- 退出
EXIT;
```

### 3. 修改数据库配置

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_writing_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # ⚠️ 改为你的MySQL用户名
    password: your_password # ⚠️ 改为你的MySQL密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update  # 自动创建/更新表结构
    show-sql: true      # 显示SQL语句（开发时）
    
ai:
  api:
    key: YOUR_API_KEY   # ⚠️ 改为你的AI API密钥
```

---

## 🔧 第二步：启动后端服务

### 方式1：使用 IDEA (推荐) ⭐

1. **打开项目**
   - 用 IntelliJ IDEA 打开 `backend` 文件夹
   - 等待 Maven 自动下载依赖

2. **配置运行**
   - 找到 `AiWritingApplication.java`
   - 右键 → Run 'AiWritingApplication'

3. **查看启动日志**
   ```
   🚀 AI 五感作文训练平台后端已启动成功！
   🌐 API 地址：http://localhost:8080
   ```

### 方式2：使用命令行

```bash
# 进入后端目录
cd backend

# 清理并编译（首次运行）
mvn clean install -DskipTests

# 启动服务
mvn spring-boot:run

# 或者使用包装脚本（Windows）
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

### 方式3：打包运行

```bash
# 打包成 JAR
mvn clean package -DskipTests

# 运行 JAR
java -jar target/write-0.0.1-SNAPSHOT.jar
```

### ✅ 后端启动成功标志

浏览器访问：`http://localhost:8080`
- 看到页面说明后端启动成功！

测试 API：
```bash
# 测试健康检查（如果有）
curl http://localhost:8080/actuator/health
```

---

## 💻 第三步：启动前端服务

### 1. 安装依赖

```bash
# 进入前端目录
cd frontend

# 安装所有依赖（首次运行，需要1-2分钟）
npm install

# 如果速度慢，使用国内镜像
npm config set registry https://registry.npmmirror.com
npm install
```

### 2. 启动开发服务器

```bash
# 启动前端
npm run dev
```

### 3. 查看启动信息

```bash
VITE v5.2.0  ready in 500 ms

➜  Local:   http://localhost:5173/
➜  Network: use --host to expose
➜  press h to show help
```

### 4. 访问应用

在浏览器中打开：`http://localhost:5173`

### ✅ 前端启动成功标志

- 能看到登录页面
- 页面有渐变背景
- 标题显示"AI五感作文训练平台"

---

## 🎯 第四步：测试系统

### 1. 注册新用户

1. 点击"立即注册"
2. 填写信息：
   ```
   用户名: student01
   密码: 123456
   确认密码: 123456
   角色: 学生
   ```
3. 点击"注册"按钮
4. 看到"注册成功"提示

### 2. 登录系统

1. 返回登录页
2. 输入刚才注册的账号
3. 选择角色：学生
4. 点击"登录"
5. 自动跳转到学生首页

### 3. 测试学生端功能

#### 测试1：写作文并获取AI反馈

1. 在"写作文"标签下：
   ```
   作文题目: 我的暑假生活
   作文内容: 今年暑假，我去了海边...（随便写点内容）
   ```
2. 点击"提交并获取AI反馈"
3. 等待几秒，下方会显示AI的反馈

#### 测试2：获取写作灵感

1. 切换到"获取灵感"标签
2. 输入题目：
   ```
   作文题目: 我最喜欢的季节
   ```
3. 点击"生成写作灵感"
4. AI会生成写作提纲

#### 测试3：查看右侧功能

- 查看成长曲线（可能暂无数据）
- 查看激励语列表
- 查看历史作文记录

### 4. 测试教师端功能

1. 退出登录
2. 注册教师账号：
   ```
   用户名: teacher01
   密码: 123456
   角色: 教师
   ```
3. 登录教师端
4. 查看学生列表
5. 点击学生查看作文
6. 提交批改意见

### 5. 测试家长端功能

1. 退出登录
2. 注册家长账号：
   ```
   用户名: parent01
   密码: 123456
   角色: 家长
   ```
3. 登录家长端
4. 查看孩子列表
5. 查看孩子成长曲线
6. 发送激励语

---

## 🐛 常见问题解决

### 问题1: 后端启动失败 - 数据库连接错误

**错误信息**:
```
Access denied for user 'root'@'localhost'
```

**解决方案**:
1. 检查 `application.yml` 中的用户名和密码
2. 确认 MySQL 服务已启动
3. 确认数据库 `ai_writing_db` 已创建

### 问题2: 后端启动失败 - 端口被占用

**错误信息**:
```
Port 8080 is already in use
```

**解决方案**:
```bash
# Windows - 查找占用8080端口的进程
netstat -ano | findstr :8080

# 结束进程（替换PID）
taskkill /PID <进程ID> /F

# 或修改 application.yml 中的端口
server:
  port: 8081
```

### 问题3: 前端安装依赖失败

**错误信息**:
```
npm ERR! code ECONNREFUSED
```

**解决方案**:
```bash
# 清除缓存
npm cache clean --force

# 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 重新安装
rm -rf node_modules package-lock.json
npm install
```

### 问题4: 前端无法连接后端

**错误信息**: 浏览器控制台显示 `404` 或 `Network Error`

**解决方案**:
1. 确认后端已启动在 `http://localhost:8080`
2. 检查 `vite.config.js` 中的代理配置
3. 打开浏览器开发者工具 (F12) 查看 Network 标签
4. 确认请求地址是否正确

### 问题5: AI反馈不工作

**可能原因**:
- API Key 未配置或无效
- 网络无法访问AI服务

**解决方案**:
1. 检查 `application.yml` 中的 `ai.api.key`
2. 确认API Key是否有效
3. 测试网络连接

### 问题6: 页面空白

**解决方案**:
1. 打开浏览器开发者工具 (F12)
2. 查看 Console 标签的错误信息
3. 检查 Network 标签的请求状态
4. 清除浏览器缓存 (Ctrl + Shift + Delete)
5. 硬刷新页面 (Ctrl + F5)

---

## 📊 启动检查清单

使用这个清单确保一切正常：

### 后端检查 ✅
- [ ] MySQL 服务已启动
- [ ] 数据库 `ai_writing_db` 已创建
- [ ] `application.yml` 配置正确
- [ ] 后端服务启动成功（8080端口）
- [ ] 能访问 `http://localhost:8080`

### 前端检查 ✅
- [ ] Node.js 已安装
- [ ] npm 依赖已安装
- [ ] 前端服务启动成功（5173端口）
- [ ] 能访问 `http://localhost:5173`
- [ ] 能看到登录页面

### 功能检查 ✅
- [ ] 能注册新用户
- [ ] 能登录系统
- [ ] 能提交作文
- [ ] 能获取AI反馈
- [ ] 三种角色都能正常切换

---

## 🎯 完整启动流程总结

### 一键启动（推荐）

**终端1 - 后端**:
```bash
cd backend
mvn spring-boot:run
```

**终端2 - 前端**:
```bash
cd frontend
npm run dev
```

**浏览器**:
```
打开: http://localhost:5173
```

### 启动顺序
```
1. MySQL 数据库
   ↓
2. 后端服务 (8080)
   ↓
3. 前端服务 (5173)
   ↓
4. 浏览器访问
```

---

## 🛠️ 开发模式

### 后端开发
- 修改 Java 代码后，IDEA 会自动重新编译
- 或使用 Spring Boot DevTools 实现热重载

### 前端开发
- 修改代码后，Vite 会自动热更新
- 浏览器会自动刷新，无需手动操作

---

## 📚 其他文档

- `README.md` - 项目说明
- `QUICKSTART.md` - 快速开始
- `PROJECT_STRUCTURE.md` - 项目结构
- `PROJECT_COMPLETE.md` - 完成报告

---

## 💡 开发技巧

### 1. 同时查看后端和前端日志
使用两个终端窗口，一个运行后端，一个运行前端

### 2. 使用浏览器开发者工具
- F12 打开开发者工具
- Network 标签查看API请求
- Console 标签查看日志和错误

### 3. 数据库管理工具
推荐使用：
- MySQL Workbench
- DBeaver
- Navicat

---

## 🎉 成功启动标志

当你看到：
- ✅ 后端控制台: "🚀 AI 五感作文训练平台后端已启动成功！"
- ✅ 前端终端: "➜  Local:   http://localhost:5173/"
- ✅ 浏览器: 显示登录页面，界面美观

**恭喜！你的项目已经成功启动！** 🎊

---

## 📞 遇到问题？

1. 查看对应的错误信息
2. 参考本文档的"常见问题解决"部分
3. 检查浏览器开发者工具的错误信息
4. 查看后端控制台的日志

**祝你开发顺利！** 🚀✨


























