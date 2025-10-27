<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- Logo和标题 -->
      <div class="navbar-brand">
        <el-icon :size="28" color="#fff"><Edit /></el-icon>
        <span class="brand-text">AI五感作文训练平台</span>
      </div>

      <!-- 用户信息 -->
      <div class="navbar-user">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="36">{{ userStore.username.charAt(0) }}</el-avatar>
            <span class="username">{{ userStore.username }}</span>
            <el-tag :type="getRoleType()" size="small" effect="dark">
              {{ getRoleText() }}
            </el-tag>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :icon="User" disabled>
                {{ userStore.username }}
              </el-dropdown-item>
              <el-dropdown-item divided :icon="Setting" command="settings">
                个人设置
              </el-dropdown-item>
              <el-dropdown-item :icon="SwitchButton" command="logout">
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { 
  Edit, ArrowDown, User, Setting, SwitchButton 
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const getRoleText = () => {
  const roleMap = {
    STUDENT: '学生',
    TEACHER: '教师',
    PARENT: '家长'
  }
  return roleMap[userStore.role] || '未知'
}

const getRoleType = () => {
  const typeMap = {
    STUDENT: 'primary',
    TEACHER: 'success',
    PARENT: 'warning'
  }
  return typeMap[userStore.role] || 'info'
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      await userStore.logout()
      router.push('/login')
    } catch (error) {
      // 用户取消
    }
  } else if (command === 'settings') {
    ElMessage.info('个人设置功能开发中...')
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.navbar-container {
  max-width: 1400px;
  margin: 0 auto;
  height: 100%;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.brand-text {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  letter-spacing: 1px;
}

.navbar-user {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 15px;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
}

.username {
  color: #fff;
  font-weight: 500;
  font-size: 14px;
}

.dropdown-icon {
  color: #fff;
  transition: transform 0.3s;
}

.user-info:hover .dropdown-icon {
  transform: rotate(180deg);
}

@media (max-width: 768px) {
  .brand-text {
    display: none;
  }
  
  .username {
    display: none;
  }
}
</style>


























