<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>✍️ 创建新账户</h2>
          <p class="subtitle">加入AI五感作文训练平台</p>
        </div>
      </template>

      <el-form 
        ref="registerFormRef" 
        :model="registerForm" 
        :rules="rules" 
        label-width="80px"
        class="register-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名（3-20个字符）"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="账号" prop="account">
          <el-input 
            v-model="registerForm.account" 
            placeholder="请输入账号（3-20个字符）"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码（至少6个字符）"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="学生" value="STUDENT">
              <span>👨‍🎓 学生 - 提交作文，获取AI反馈</span>
            </el-option>
            <el-option label="教师" value="TEACHER">
              <span>👨‍🏫 教师 - 批改作文，追踪进步</span>
            </el-option>
            <el-option label="家长" value="PARENT">
              <span>👨‍👩‍👧 家长 - 查看成长，发送激励</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item 
          v-if="registerForm.role === 'STUDENT'" 
          label="学历" 
          prop="educationLevel"
        >
          <el-select 
            v-model="registerForm.educationLevel" 
            placeholder="请选择学历" 
            style="width: 100%"
            @change="handleEducationLevelChange"
          >
            <el-option label="小学" value="PRIMARY" />
            <el-option label="初中" value="MIDDLE" />
          </el-select>
        </el-form-item>

        <el-form-item 
          v-if="registerForm.role === 'STUDENT' && registerForm.educationLevel" 
          label="年级" 
          prop="grade"
        >
          <el-select 
            v-model="registerForm.grade" 
            placeholder="请选择年级" 
            style="width: 100%"
          >
            <el-option 
              v-for="grade in availableGrades" 
              :key="grade.value" 
              :label="grade.label" 
              :value="grade.value" 
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleRegister"
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>

        <div class="footer-links">
          <span>已有账户？</span>
          <el-link type="primary" @click="goToLogin">立即登录</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  account: '',
  password: '',
  confirmPassword: '',
  role: 'STUDENT',
  educationLevel: '',
  grade: ''
})

// 根据学历动态生成年级选项
const availableGrades = computed(() => {
  if (registerForm.educationLevel === 'PRIMARY') {
    return [
      { label: '一年级', value: 'GRADE_1' },
      { label: '二年级', value: 'GRADE_2' },
      { label: '三年级', value: 'GRADE_3' },
      { label: '四年级', value: 'GRADE_4' },
      { label: '五年级', value: 'GRADE_5' },
      { label: '六年级', value: 'GRADE_6' }
    ]
  } else if (registerForm.educationLevel === 'MIDDLE') {
    return [
      { label: '一年级', value: 'GRADE_1' },
      { label: '二年级', value: 'GRADE_2' },
      { label: '三年级', value: 'GRADE_3' }
    ]
  }
  return []
})

// 当学历改变时，清空年级选择
const handleEducationLevelChange = () => {
  registerForm.grade = ''
}

// 当角色改变时，清空学历和年级
watch(() => registerForm.role, (newRole) => {
  if (newRole !== 'STUDENT') {
    registerForm.educationLevel = ''
    registerForm.grade = ''
  }
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  educationLevel: [
    { 
      validator: (rule, value, callback) => {
        if (registerForm.role === 'STUDENT' && !value) {
          callback(new Error('请选择学历'))
        } else {
          callback()
        }
      }, 
      trigger: 'change' 
    }
  ],
  grade: [
    { 
      validator: (rule, value, callback) => {
        if (registerForm.role === 'STUDENT' && registerForm.educationLevel && !value) {
          callback(new Error('请选择年级'))
        } else {
          callback()
        }
      }, 
      trigger: 'change' 
    }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { confirmPassword, ...data } = registerForm
        const success = await userStore.register(data)
        if (success) {
          setTimeout(() => {
            router.push('/login')
          }, 1500)
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: white;
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 450px;
  border-radius: 8px;
  box-shadow: none;
  background: white;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
}

.subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.register-form {
  margin-top: 20px;
}

.footer-links {
  text-align: center;
  margin-top: 20px;
}

.footer-links span {
  color: #606266;
  margin-right: 8px;
}
</style>


























