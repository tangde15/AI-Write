# AI五感作文训练平台 - 前端

## 📖 项目简介

这是一个基于 Vue3 + Vite + Element Plus 的现代化前端应用，配合后端实现 AI 辅助的五感作文训练平台。

## ✨ 功能特性

### 🎓 学生端
- ✍️ 作文提交与 AI 实时反馈
- 💡 写作灵感生成（基于题目）
- 📊 个人成长曲线可视化
- ⭐ 查看来自教师、家长和 AI 的激励语
- 📝 作文历史记录查看

### 👨‍🏫 教师端
- 👥 学生列表管理
- 📄 学生作文查看与批改
- 📈 学生进步统计分析
- 💬 发送激励语给学生
- 🔍 学生写作能力追踪

### 👨‍👩‍👧 家长端
- 👶 孩子列表管理
- 📊 孩子成长曲线查看
- 📝 孩子写作记录浏览
- 💝 发送激励语给孩子
- 📈 查看孩子学习进步

## 🛠️ 技术栈

- **框架**: Vue 3.4 (Composition API)
- **构建工具**: Vite 5
- **UI 组件库**: Element Plus 2.6
- **状态管理**: Pinia 2.1
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios 1.6
- **图表库**: ECharts 5.5

## 📦 安装依赖

```bash
cd frontend
npm install
```

## 🚀 运行项目

### 开发模式
```bash
npm run dev
```
项目将运行在 `http://localhost:5173`

### 生产构建
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

## 📁 项目结构

```
frontend/
├── src/
│   ├── api/                    # API 接口层
│   │   ├── auth.js            # 认证相关
│   │   ├── student.js         # 学生端接口
│   │   ├── teacher.js         # 教师端接口
│   │   ├── parent.js          # 家长端接口
│   │   └── encouragement.js   # 激励语接口
│   ├── components/            # 公共组件
│   │   ├── NavBar.vue         # 导航栏
│   │   └── ChartProgress.vue  # 进度图表
│   ├── pages/                 # 页面组件
│   │   ├── Auth/              # 认证页面
│   │   │   ├── Login.vue
│   │   │   └── Register.vue
│   │   ├── Student/           # 学生页面
│   │   │   └── StudentHome.vue
│   │   ├── Teacher/           # 教师页面
│   │   │   └── TeacherDashboard.vue
│   │   ├── Parent/            # 家长页面
│   │   │   └── ParentHome.vue
│   │   └── Common/            # 公共页面
│   │       └── EncouragementList.vue
│   ├── router/                # 路由配置
│   │   └── index.js
│   ├── store/                 # 状态管理
│   │   └── user.js
│   ├── App.vue                # 根组件
│   └── main.js                # 入口文件
├── index.html                 # HTML 模板
├── package.json               # 依赖配置
├── vite.config.js             # Vite 配置
└── README.md                  # 项目说明
```

## 🔧 配置说明

### API 代理配置
在 `vite.config.js` 中已配置代理，所有 `/api` 请求会转发到后端：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

### 路由守卫
已实现基于角色的路由守卫，自动验证用户权限并重定向。

## 👥 用户角色

- **STUDENT** (学生): 提交作文、获取 AI 反馈
- **TEACHER** (教师): 批改作文、追踪学生进步
- **PARENT** (家长): 查看孩子成长、发送激励

## 🎨 UI 设计特点

- 🎭 渐变背景，现代化视觉设计
- 📱 响应式布局，支持移动端
- 🌈 角色区分的颜色主题
- ✨ 流畅的动画过渡效果
- 🎯 清晰的信息层级

## 🔐 认证机制

- JWT Token 认证
- 本地存储用户信息
- 自动刷新登录状态
- 路由级权限控制

## 📊 数据可视化

使用 ECharts 实现：
- 写作能力成长趋势图
- 双坐标轴（分数 + 提升率）
- 平滑曲线与渐变填充
- 响应式图表自适应

## 🚦 开发建议

1. **开发顺序**: 先启动后端服务，再启动前端项目
2. **调试工具**: 使用 Vue DevTools 进行组件调试
3. **代码规范**: 遵循 Vue 3 Composition API 最佳实践
4. **组件复用**: 充分利用 Element Plus 组件库

## 📝 API 文档

所有 API 请求需要携带 Token（除登录/注册外）：
```javascript
headers: {
  Authorization: `Bearer ${token}`
}
```

## 🐛 常见问题

### Q: 无法连接后端？
A: 确保后端服务已启动在 `http://localhost:8080`

### Q: 路由跳转失败？
A: 检查用户角色是否与路由权限匹配

### Q: 图表不显示？
A: 检查数据格式是否正确，需要包含 `date`, `avgScore`, `improvementRate` 字段

## 📄 License

MIT

## 👨‍💻 作者

AI五感作文训练平台开发团队

---

**祝您使用愉快！如有问题，欢迎提 Issue。** ✨














