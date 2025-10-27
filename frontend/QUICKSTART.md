# 🚀 快速开始指南

## 前置要求

- Node.js >= 16.0.0
- npm >= 8.0.0
- 后端服务已启动在 `http://localhost:8080`

## 快速启动步骤

### 1️⃣ 安装依赖

```bash
cd frontend
npm install
```

安装时间约 1-2 分钟，请耐心等待。

### 2️⃣ 启动开发服务器

```bash
npm run dev
```

成功后会显示：
```
VITE v5.2.0  ready in 500 ms

➜  Local:   http://localhost:5173/
➜  Network: use --host to expose
```

### 3️⃣ 访问应用

在浏览器中打开：`http://localhost:5173`

## 🎯 快速测试流程

### 注册新用户

1. 点击"立即注册"
2. 填写信息：
   - 用户名：`student01`
   - 密码：`123456`
   - 角色：选择"学生"
3. 点击"注册"按钮

### 登录系统

1. 使用刚注册的账号登录
2. 系统会自动跳转到对应角色的首页

### 学生端测试

1. **写作文**：
   - 输入题目：`我的暑假生活`
   - 写作文内容
   - 点击"提交并获取AI反馈"

2. **获取灵感**：
   - 切换到"获取灵感"标签
   - 输入题目
   - 点击"生成写作灵感"

3. **查看进度**：
   - 右侧可查看成长曲线
   - 查看激励语列表
   - 浏览历史作文

### 教师端测试

使用教师角色注册并登录：

1. 查看学生列表
2. 点击学生查看作文
3. 提交批改意见
4. 发送激励语

### 家长端测试

使用家长角色注册并登录：

1. 查看孩子列表
2. 查看孩子成长曲线
3. 浏览写作记录
4. 发送激励语

## 🔧 开发调试

### 安装 Vue DevTools

推荐安装浏览器扩展：
- Chrome: [Vue.js devtools](https://chrome.google.com/webstore/detail/vuejs-devtools/)
- Firefox: [Vue.js devtools](https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/)

### 查看网络请求

打开浏览器开发者工具 (F12)，查看 Network 标签页，可以看到所有 API 请求。

### 检查状态管理

使用 Vue DevTools 的 Pinia 标签，可以实时查看和修改应用状态。

## 📦 构建生产版本

```bash
npm run build
```

构建完成后，文件会输出到 `dist` 目录。

## 🐛 常见问题排查

### 问题1: npm install 失败

**解决方案**：
```bash
# 清除缓存
npm cache clean --force

# 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 重新安装
npm install
```

### 问题2: 端口被占用

**解决方案**：
修改 `vite.config.js` 中的端口号：
```javascript
server: {
  port: 5174  // 改为其他端口
}
```

### 问题3: 无法连接后端

**检查清单**：
- ✅ 后端服务是否已启动
- ✅ 后端端口是否为 8080
- ✅ 浏览器控制台是否有 CORS 错误
- ✅ `vite.config.js` 中的代理配置是否正确

### 问题4: 页面空白

**排查步骤**：
1. 打开浏览器控制台查看错误信息
2. 检查是否有 JavaScript 错误
3. 确认所有依赖已正确安装
4. 尝试清除浏览器缓存

### 问题5: 图表不显示

**可能原因**：
- 数据格式不正确
- ECharts 未正确初始化
- 容器高度为 0

**解决方案**：
检查传入 ChartProgress 组件的数据格式：
```javascript
[
  {
    date: "2024-01-01T00:00:00",
    avgScore: 85,
    improvementRate: 5.2
  }
]
```

## 💡 开发技巧

### 热更新

修改代码后会自动刷新浏览器，无需手动刷新。

### 格式化代码

推荐安装 VS Code 扩展：
- Volar (Vue 3 支持)
- ESLint
- Prettier

### 使用 Console 调试

在代码中添加：
```javascript
console.log('调试信息', variable)
```

## 📚 学习资源

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Element Plus 组件库](https://element-plus.org/zh-CN/)
- [Vite 构建工具](https://cn.vitejs.dev/)
- [Pinia 状态管理](https://pinia.vuejs.org/zh/)

## 🎉 开发完成

如果所有步骤都成功，你现在应该可以：
- ✅ 访问登录页面
- ✅ 注册新用户
- ✅ 登录不同角色
- ✅ 使用各个功能模块
- ✅ 查看数据可视化

**祝你开发愉快！** 🚀


























