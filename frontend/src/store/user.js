import { defineStore } from 'pinia'
import { authAPI } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    isLoggedIn: false,
    role: null
  }),

  getters: {
    username: (state) => state.user?.username || '',
    userId: (state) => state.user?.id || null
  },

  actions: {
    // 检查认证状态
    async checkAuth() {
      const token = localStorage.getItem('token')
      const userStr = localStorage.getItem('user')
      
      if (token && userStr) {
        try {
          this.token = token
          this.user = JSON.parse(userStr)
          this.role = this.user.role
          this.isLoggedIn = true
        } catch (error) {
          this.logout()
        }
      }
    },

    // 登录
    async login(credentials) {
      try {
        const response = await authAPI.login(credentials)
        
        if (response.token) {
          this.token = response.token
          this.user = response.user
          this.role = response.user.role
          this.isLoggedIn = true
          
          localStorage.setItem('token', response.token)
          localStorage.setItem('user', JSON.stringify(response.user))
          
          ElMessage.success('登录成功！')
          return true
        }
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败')
        return false
      }
    },

    // 注册
    async register(userData) {
      try {
        const response = await authAPI.register(userData)
        ElMessage.success('注册成功，请登录')
        return true
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '注册失败')
        return false
      }
    },

    // 退出登录
    async logout() {
      try {
        await authAPI.logout()
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.user = null
        this.token = null
        this.role = null
        this.isLoggedIn = false
        
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        
        ElMessage.success('已退出登录')
      }
    }
  }
})














