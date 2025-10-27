# 部署指南 - 在其他主机上部署AI五感作文训练平台

## 🎯 部署概述

本指南将帮助您在其他主机上成功部署AI五感作文训练平台。我们提供了详细的步骤说明，确保您能够快速搭建完整的运行环境。

## 📋 系统要求

### 最低配置
- **CPU**: 2核心
- **内存**: 2GB RAM
- **存储**: 10GB可用空间
- **网络**: 稳定的互联网连接

### 推荐配置
- **CPU**: 4核心
- **内存**: 4GB RAM
- **存储**: 20GB可用空间
- **网络**: 100Mbps带宽

## 🛠️ 环境准备

### 1. 操作系统支持
- Ubuntu 18.04+ / 20.04+ / 22.04+
- CentOS 7+ / 8+
- RHEL 7+ / 8+
- Windows Server 2016+

### 2. 安装必要软件

#### Ubuntu/Debian系统
```bash
# 更新系统包
sudo apt update && sudo apt upgrade -y

# 安装JDK 17
sudo apt install openjdk-17-jdk -y

# 安装Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 安装MySQL 8.0
sudo apt install mysql-server-8.0 -y

# 安装Maven
sudo apt install maven -y

# 安装Nginx（可选，用于前端部署）
sudo apt install nginx -y
```

#### CentOS/RHEL系统
```bash
# 更新系统包
sudo yum update -y

# 安装JDK 17
sudo yum install java-17-openjdk java-17-openjdk-devel -y

# 安装Node.js 18
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install nodejs -y

# 安装MySQL 8.0
sudo yum install mysql-server -y

# 安装Maven
sudo yum install maven -y

# 安装Nginx（可选）
sudo yum install nginx -y
```

## 🗄️ 数据库配置

### 1. 启动MySQL服务
```bash
# Ubuntu/Debian
sudo systemctl start mysql
sudo systemctl enable mysql

# CentOS/RHEL
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

### 2. 安全配置MySQL
```bash
sudo mysql_secure_installation
```

### 3. 创建数据库和用户
```bash
sudo mysql -u root -p
```

在MySQL命令行中执行：
```sql
-- 创建数据库
CREATE DATABASE aiwriting CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建专用用户
CREATE USER 'aiwriting_user'@'localhost' IDENTIFIED BY 'your_secure_password_here';

-- 授权
GRANT ALL PRIVILEGES ON aiwriting.* TO 'aiwriting_user'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;

-- 退出
EXIT;
```

### 4. 导入数据库结构
```bash
# 导入建表脚本
mysql -u aiwriting_user -p aiwriting < backend/src/main/resources/schema.sql

# 导入测试数据（可选）
mysql -u aiwriting_user -p aiwriting < backend/src/main/resources/test-data.sql
```

## ⚙️ 应用配置

### 1. 后端配置

编辑 `backend/src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aiwriting?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: aiwriting_user
    password: your_secure_password_here
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false  # 生产环境建议关闭
    properties:
      hibernate.format_sql: false
      hibernate.dialect: org.hibernate.dialect.MySQLDialect

  sql:
    init:
      mode: never
      encoding: UTF-8

# AI API配置 - 需要替换为你的API密钥
ai:
  api:
    url: https://api.siliconflow.cn/v1/chat/completions
    key: sk-your-siliconflow-api-key-here

# 日志配置
logging:
  level:
    root: WARN
    com.write.write: INFO
  file:
    name: logs/aiwriting.log
```

### 2. 前端配置

编辑 `frontend/src/api/auth.js`:

```javascript
import axios from 'axios'

// 修改为你的服务器地址
const API_BASE_URL = 'http://your-server-ip:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  withCredentials: true
})

// ... 其他配置保持不变
```

## 🚀 构建和部署

### 1. 后端构建
```bash
cd backend

# 清理并编译
mvn clean compile

# 运行测试（可选）
mvn test

# 打包应用
mvn package -DskipTests

# 启动应用
java -jar target/Write-0.0.1-SNAPSHOT.jar
```

### 2. 前端构建
```bash
cd frontend

# 安装依赖
npm install

# 构建生产版本
npm run build

# 构建完成后，dist目录包含所有静态文件
```

### 3. 使用Nginx部署前端（推荐）

创建Nginx配置文件 `/etc/nginx/sites-available/aiwriting`:

```nginx
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名或IP

    # 前端静态文件
    location / {
        root /path/to/frontend/dist;  # 替换为实际的dist目录路径
        try_files $uri $uri/ /index.html;
        index index.html;
    }

    # API代理到后端
    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

启用配置：
```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/aiwriting /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重启Nginx
sudo systemctl restart nginx
```

## 🔧 系统服务配置

### 1. 创建后端服务

创建服务文件 `/etc/systemd/system/aiwriting-backend.service`:

```ini
[Unit]
Description=AI Writing Platform Backend
After=network.target mysql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/path/to/backend
ExecStart=/usr/bin/java -jar target/Write-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
# 重新加载systemd配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start aiwriting-backend

# 设置开机自启
sudo systemctl enable aiwriting-backend

# 查看服务状态
sudo systemctl status aiwriting-backend
```

## 🔒 安全配置

### 1. 防火墙配置
```bash
# Ubuntu/Debian (ufw)
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw enable

# CentOS/RHEL (firewalld)
sudo firewall-cmd --permanent --add-service=ssh
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

### 2. SSL证书配置（可选）

使用Let's Encrypt免费SSL证书：
```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加：0 12 * * * /usr/bin/certbot renew --quiet
```

## 📊 监控和日志

### 1. 日志配置
```bash
# 创建日志目录
sudo mkdir -p /var/log/aiwriting
sudo chown www-data:www-data /var/log/aiwriting

# 查看应用日志
sudo journalctl -u aiwriting-backend -f

# 查看Nginx日志
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### 2. 性能监控
```bash
# 安装htop
sudo apt install htop -y

# 监控系统资源
htop

# 监控MySQL
sudo mysql -u root -p
SHOW PROCESSLIST;
```

## 🧪 测试部署

### 1. 后端测试
```bash
# 测试API连接
curl http://localhost:8080/api/auth/login

# 测试数据库连接
curl http://localhost:8080/api/writing/records
```

### 2. 前端测试
```bash
# 访问前端页面
curl http://your-server-ip

# 检查静态资源
curl http://your-server-ip/assets/index.js
```

## 🔄 更新和维护

### 1. 应用更新
```bash
# 停止服务
sudo systemctl stop aiwriting-backend

# 备份当前版本
sudo cp target/Write-0.0.1-SNAPSHOT.jar target/Write-0.0.1-SNAPSHOT.jar.backup

# 更新代码
git pull origin main

# 重新构建
mvn clean package -DskipTests

# 启动服务
sudo systemctl start aiwriting-backend
```

### 2. 数据库备份
```bash
# 创建备份脚本
sudo nano /usr/local/bin/backup-aiwriting.sh
```

```bash
#!/bin/bash
BACKUP_DIR="/backup/aiwriting"
DATE=$(date +%Y%m%d_%H%M%S)
mkdir -p $BACKUP_DIR

mysqldump -u aiwriting_user -p aiwriting > $BACKUP_DIR/aiwriting_$DATE.sql
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

```bash
# 设置定时备份
sudo chmod +x /usr/local/bin/backup-aiwriting.sh
sudo crontab -e
# 添加：0 2 * * * /usr/local/bin/backup-aiwriting.sh
```

## ❗ 常见问题

### Q: 数据库连接失败
**A**: 检查MySQL服务状态、用户名密码、数据库是否存在
```bash
sudo systemctl status mysql
mysql -u aiwriting_user -p -e "SHOW DATABASES;"
```

### Q: 前端无法访问后端API
**A**: 检查防火墙设置、Nginx配置、后端服务状态
```bash
sudo systemctl status aiwriting-backend
sudo nginx -t
curl http://localhost:8080/api/auth/login
```

### Q: AI API调用失败
**A**: 检查API密钥是否正确、网络连接是否正常
```bash
curl -H "Authorization: Bearer your-api-key" https://api.siliconflow.cn/v1/models
```

### Q: 内存不足
**A**: 增加服务器内存或调整JVM参数
```bash
java -Xms512m -Xmx1024m -jar target/Write-0.0.1-SNAPSHOT.jar
```

## 📞 技术支持

如果在部署过程中遇到问题，请：

1. 查看日志文件获取详细错误信息
2. 检查配置文件是否正确
3. 确认所有依赖服务正常运行
4. 提交Issue到项目仓库

---

**部署完成后，您就可以通过浏览器访问 `http://your-server-ip` 来使用AI五感作文训练平台了！**














