<template>
  <div class="login-container">
    <!-- 左侧猫咪图片 -->
    <div class="left-section">
      <img src="/cat-image.jpg" alt="猫咪" class="cat-image" />
    </div>
    
    <!-- 右侧登录表单 -->
    <div class="right-section">
      <div class="login-form-container">
        <div class="form-header">
          <h1 class="login-title">登录</h1>
          <div class="header-links">
            <a href="#" @click="goToRegister" class="link">注册</a>
            <a href="#" @click="showComingSoon" class="link">其他方式登录</a>
          </div>
        </div>

        <el-form 
          ref="loginFormRef" 
          :model="loginForm" 
          :rules="rules" 
          class="login-form"
        >
          <el-form-item prop="account">
            <el-input 
              v-model="loginForm.account" 
              placeholder="账号"
              :prefix-icon="User"
              class="form-input"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="密码"
              :prefix-icon="Lock"
              show-password
              class="form-input"
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item prop="role">
            <el-select v-model="loginForm.role" placeholder="三端选择" class="form-input">
              <el-option label="学生" value="STUDENT" />
              <el-option label="教师" value="TEACHER" />
              <el-option label="家长" value="PARENT" />
            </el-select>
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="rememberMe" class="remember-checkbox">记住我</el-checkbox>
            <a href="#" @click="showComingSoon" class="forgot-password">忘记密码?</a>
          </div>

          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleLogin"
            class="login-button"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive({
  account: '',
  password: '',
  role: 'STUDENT'
})

const rules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 检查记住我功能
onMounted(() => {
  const savedAccount = localStorage.getItem('rememberedAccount')
  const savedRole = localStorage.getItem('rememberedRole')
  if (savedAccount && savedRole) {
    loginForm.account = savedAccount
    loginForm.role = savedRole
    rememberMe.value = true
  }
})

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await userStore.login(loginForm)
        if (success) {
          // 如果勾选了记住我，保存账号信息
          if (rememberMe.value) {
            localStorage.setItem('rememberedAccount', loginForm.account)
            localStorage.setItem('rememberedRole', loginForm.role)
          } else {
            localStorage.removeItem('rememberedAccount')
            localStorage.removeItem('rememberedRole')
          }
          
          // 根据角色跳转到对应页面
          const roleRoutes = {
            STUDENT: '/student',
            TEACHER: '/teacher',
            PARENT: '/parent'
          }
          router.push(roleRoutes[loginForm.role])
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/register')
}

const showComingSoon = () => {
  ElMessage.info('该功能正在开发中')
}
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.left-section {
  flex: 1;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.cat-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.right-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  padding: 30px 40px;
  overflow-y: auto;
}

.login-form-container {
  width: 100%;
  max-width: 380px;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.header-links {
  display: flex;
  gap: 15px;
}

.link {
  color: #409eff;
  text-decoration: none;
  font-size: 13px;
  transition: color 0.3s;
}

.link:hover {
  color: #66b1ff;
}

.login-form {
  width: 100%;
}

.form-input {
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0;
}

.remember-checkbox {
  color: #666;
  font-size: 13px;
}

.forgot-password {
  color: #409eff;
  text-decoration: none;
  font-size: 13px;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: #66b1ff;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 15px;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .left-section {
    height: 40vh;
  }
  
  .right-section {
    height: 60vh;
    padding: 20px;
  }
  
  .form-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .header-links {
    justify-content: center;
  }
}
</style>