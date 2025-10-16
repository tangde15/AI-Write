import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 900000  // 15分钟
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  // 用户注册
  register(data) {
    return api.post('/auth/register', data)
  },

  // 用户登录
  login(data) {
    return api.post('/auth/login', data)
  },

  // 获取当前用户信息
  getUserInfo() {
    return api.get('/auth/info')
  },

  // 退出登录
  logout() {
    return api.post('/auth/logout')
  }
}

export default api

