# 📂 项目结构说明

## 完整目录树

```
frontend/
│
├── 📄 index.html                      # HTML 入口文件
├── 📄 package.json                    # 项目依赖配置
├── 📄 vite.config.js                  # Vite 构建配置
├── 📄 README.md                       # 项目说明文档
├── 📄 QUICKSTART.md                   # 快速开始指南
├── 📄 .gitignore                      # Git 忽略配置
│
└── 📁 src/                            # 源代码目录
    │
    ├── 📄 main.js                     # 应用入口（Vue 实例创建）
    ├── 📄 App.vue                     # 根组件
    │
    ├── 📁 router/                     # 路由配置
    │   └── 📄 index.js                # 路由定义 + 路由守卫
    │
    ├── 📁 store/                      # 状态管理（Pinia）
    │   └── 📄 user.js                 # 用户状态管理
    │
    ├── 📁 api/                        # API 接口层
    │   ├── 📄 auth.js                 # 认证接口 + Axios 配置
    │   ├── 📄 student.js              # 学生端接口
    │   ├── 📄 teacher.js              # 教师端接口
    │   ├── 📄 parent.js               # 家长端接口
    │   └── 📄 encouragement.js        # 激励语接口
    │
    ├── 📁 components/                 # 公共组件
    │   ├── 📄 NavBar.vue              # 顶部导航栏
    │   └── 📄 ChartProgress.vue       # 进度图表组件（ECharts）
    │
    └── 📁 pages/                      # 页面组件
        │
        ├── 📁 Auth/                   # 认证相关页面
        │   ├── 📄 Login.vue           # 登录页
        │   └── 📄 Register.vue        # 注册页
        │
        ├── 📁 Student/                # 学生端页面
        │   └── 📄 StudentHome.vue     # 学生首页（写作 + 查看进度）
        │
        ├── 📁 Teacher/                # 教师端页面
        │   └── 📄 TeacherDashboard.vue # 教师仪表盘（批改 + 统计）
        │
        ├── 📁 Parent/                 # 家长端页面
        │   └── 📄 ParentHome.vue      # 家长首页（查看孩子成长）
        │
        └── 📁 Common/                 # 通用页面组件
            └── 📄 EncouragementList.vue # 激励语列表组件
```

## 📦 核心文件说明

### 入口与配置

| 文件 | 作用 | 关键内容 |
|------|------|----------|
| `index.html` | HTML 模板 | 应用挂载点 `<div id="app">` |
| `main.js` | 应用入口 | 创建 Vue 实例、注册插件 |
| `vite.config.js` | 构建配置 | 路径别名、代理配置 |
| `package.json` | 依赖管理 | 项目依赖、脚本命令 |

### 路由系统

**文件**: `src/router/index.js`

**路由列表**:
```
/                    → 重定向到 /login
/login               → 登录页（游客可访问）
/register            → 注册页（游客可访问）
/student             → 学生首页（需要 STUDENT 角色）
/teacher             → 教师仪表盘（需要 TEACHER 角色）
/parent              → 家长首页（需要 PARENT 角色）
```

**路由守卫**:
- 未登录访问受保护页面 → 重定向到登录页
- 角色不匹配 → 重定向到对应角色首页
- 已登录访问登录/注册页 → 重定向到对应角色首页

### 状态管理

**文件**: `src/store/user.js`

**State**:
```javascript
{
  user: null,           // 用户信息对象
  token: null,          // JWT Token
  isLoggedIn: false,    // 登录状态
  role: null            // 用户角色
}
```

**Actions**:
- `checkAuth()` - 检查本地存储的登录状态
- `login(credentials)` - 用户登录
- `register(userData)` - 用户注册
- `logout()` - 退出登录

### API 接口层

#### `api/auth.js`
- Axios 实例配置
- 请求/响应拦截器
- Token 自动注入
- 401 错误自动处理

#### `api/student.js`
```javascript
submitWriting()      // 提交作文（AI 辅助）
getMyWritings()      // 获取我的作文历史
getProgress()        // 获取写作进度
getEncouragements()  // 获取收到的激励语
```

#### `api/teacher.js`
```javascript
getStudents()            // 获取学生列表
getStudentWritings(id)   // 获取学生作文
submitFeedback(id, fb)   // 提交批改反馈
getStudentProgress(id)   // 获取学生进步统计
sendEncouragement(id, c) // 发送激励语
```

#### `api/parent.js`
```javascript
getChildren()              // 获取孩子列表
getChildWritings(id)       // 获取孩子作文记录
getChildProgress(id)       // 获取孩子成长曲线
sendEncouragement(id, c)   // 发送激励语给孩子
getSentEncouragements(id)  // 查看已发送激励语
```

### 页面组件详解

#### 📝 StudentHome.vue (学生首页)
**布局**: 左右分栏
- **左侧**: 作文提交区
  - Tab 1: 写作文（输入题目 + 内容）
  - Tab 2: 获取灵感（根据题目生成提纲）
  - AI 反馈显示区
- **右侧**: 
  - 成长曲线图表
  - 激励语列表
  - 历史作文时间线

#### 👨‍🏫 TeacherDashboard.vue (教师仪表盘)
**布局**: 左右分栏
- **左侧**: 学生列表
  - 搜索功能
  - 学生卡片（头像 + 姓名 + 作文数）
- **右侧**: 学生详情
  - 成长曲线图表
  - 作文列表（可展开查看）
  - 批改意见输入区
  - 发送激励语按钮

#### 👨‍👩‍👧 ParentHome.vue (家长首页)
**布局**: 左右分栏
- **左侧**: 孩子列表
  - 孩子卡片（头像 + 姓名 + 作文数）
- **右侧**: 孩子详情
  - 成长曲线图表（带状态标签）
  - 写作记录时间线
  - 激励语列表
  - 发送激励语功能

### 公共组件

#### 🧭 NavBar.vue (导航栏)
- 固定在页面顶部
- 显示平台 Logo 和名称
- 用户头像 + 姓名 + 角色标签
- 下拉菜单（个人设置、退出登录）
- 渐变背景色

#### 📊 ChartProgress.vue (进度图表)
- 基于 ECharts 实现
- 双坐标轴（分数 + 提升率）
- 平滑曲线
- 渐变填充效果
- 响应式自适应
- 空状态处理

#### ⭐ EncouragementList.vue (激励语列表)
- 卡片式布局
- 角色区分颜色（教师/家长/AI）
- 发送者头像 + 姓名 + 角色标签
- 相对时间显示（刚刚、5分钟前等）
- 点赞功能
- 空状态提示

## 🔄 数据流向

```
用户操作
  ↓
Vue 组件
  ↓
API 接口层 (axios)
  ↓
后端服务器
  ↓
数据库
  ↓
响应返回
  ↓
Pinia Store（状态更新）
  ↓
Vue 组件（视图更新）
```

## 🎨 样式设计

### 颜色主题
- **主色调**: `#667eea` → `#764ba2`（渐变）
- **学生**: `#409eff` (蓝色)
- **教师**: `#67c23a` (绿色)
- **家长**: `#e6a23c` (橙色)
- **AI**: `#409eff` (蓝色)

### 响应式断点
- **桌面端**: `lg` (≥1200px) - 左右分栏布局
- **移动端**: `xs` (≤768px) - 垂直堆叠布局

## 🔐 安全机制

1. **Token 认证**: JWT Token 存储在 localStorage
2. **路由守卫**: 验证登录状态和角色权限
3. **请求拦截**: 自动注入 Token 到请求头
4. **响应拦截**: 401 错误自动跳转登录页
5. **XSS 防护**: Vue 自动转义 HTML

## 📈 性能优化

1. **路由懒加载**: 使用动态 import
2. **组件按需加载**: Element Plus 自动按需引入
3. **图表按需销毁**: ECharts 实例在组件卸载时销毁
4. **响应式图表**: 监听窗口 resize 事件
5. **Vite 优化**: 快速冷启动 + 热模块替换

## 🛠️ 开发规范

### 命名约定
- **组件**: PascalCase（如 `StudentHome.vue`）
- **文件夹**: kebab-case（如 `api/`, `pages/`）
- **变量/函数**: camelCase（如 `handleLogin`）
- **常量**: UPPER_SNAKE_CASE（如 `API_BASE_URL`）

### Composition API 使用
```vue
<script setup>
// 1. 导入依赖
import { ref, reactive, onMounted } from 'vue'

// 2. Props 和 Emits
const props = defineProps({ ... })
const emit = defineEmits(['update'])

// 3. 响应式数据
const count = ref(0)
const form = reactive({ ... })

// 4. 计算属性和侦听器
const computed = computed(() => ...)
watch(source, callback)

// 5. 方法
const handleClick = () => { ... }

// 6. 生命周期
onMounted(() => { ... })
</script>
```

## 📱 兼容性

- **浏览器**: Chrome 90+, Firefox 88+, Safari 14+, Edge 90+
- **移动端**: iOS 14+, Android 10+
- **Node.js**: 16.0.0+

---

**项目结构设计原则**: 
- ✅ 模块化分离
- ✅ 职责单一
- ✅ 易于维护
- ✅ 可扩展性强














