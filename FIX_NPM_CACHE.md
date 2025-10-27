# 修复 npm 缓存权限问题

## 问题描述
```
npm error code EPERM
npm error syscall open
npm error path E:\nodejs\node_cache\_cacache\tmp\573875bc
```

## 解决方案

### 方案1：清除缓存并重新安装（推荐）⭐

1. **以管理员身份打开命令提示符**
   - 按 `Win + X`
   - 选择"终端(管理员)"或"Windows PowerShell(管理员)"

2. **清除 npm 缓存**
   ```bash
   npm cache clean --force
   ```

3. **验证缓存已清除**
   ```bash
   npm cache verify
   ```

4. **进入项目目录**
   ```bash
   cd "E:\Project Practice\Write\frontend"
   ```

5. **重新安装**
   ```bash
   npm install
   ```

---

### 方案2：更改 npm 缓存目录

1. **创建新的缓存目录**（在你的项目目录下）
   ```bash
   mkdir "E:\Project Practice\Write\.npm-cache"
   ```

2. **设置 npm 使用新的缓存目录**
   ```bash
   npm config set cache "E:\Project Practice\Write\.npm-cache" --global
   ```

3. **验证配置**
   ```bash
   npm config get cache
   ```

4. **重新安装**
   ```bash
   cd "E:\Project Practice\Write\frontend"
   npm install
   ```

---

### 方案3：以管理员身份运行 IDEA

1. 关闭当前的 IDEA
2. 右键点击 IDEA 图标
3. 选择"以管理员身份运行"
4. 重新打开项目
5. 在 npm 面板中再次运行 install

---

### 方案4：临时关闭杀毒软件

有时候杀毒软件会阻止 npm 写入文件：

1. 临时关闭 Windows Defender 或其他杀毒软件
2. 重新运行 npm install
3. 安装完成后重新启用杀毒软件

---

### 方案5：使用国内镜像（提升速度）

```bash
# 设置淘宝镜像
npm config set registry https://registry.npmmirror.com

# 然后重新安装
npm install
```

---

## 快速修复脚本

在管理员命令提示符中执行：

```bash
# 1. 清除缓存
npm cache clean --force

# 2. 设置新的缓存目录
npm config set cache "C:\npm-cache" --global

# 3. 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 4. 进入项目
cd "E:\Project Practice\Write\frontend"

# 5. 安装依赖
npm install
```

---

## 验证安装成功

安装成功后会看到：
```
added 500+ packages in 30s
```

然后可以启动：
```bash
npm run dev
```

看到以下信息表示成功：
```
VITE v5.2.0  ready in 500 ms
➜  Local:   http://localhost:5173/
```


























