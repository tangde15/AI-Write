import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/Auth/Login.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/Auth/Register.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/student',
    name: 'StudentHome',
    component: () => import('@/pages/Student/StudentHome.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/teacher',
    name: 'TeacherDashboard',
    component: () => import('@/pages/Teacher/TeacherDashboard.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/parent',
    name: 'ParentHome',
    component: () => import('@/pages/Parent/ParentHome.vue'),
    meta: { requiresAuth: true, role: 'PARENT' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth) {
    if (!userStore.isLoggedIn) {
      next('/login')
    } else if (to.meta.role && userStore.role !== to.meta.role) {
      // 角色不匹配，重定向到对应角色首页
      next(`/${userStore.role.toLowerCase()}`)
    } else {
      next()
    }
  } else if (to.meta.requiresGuest && userStore.isLoggedIn) {
    // 已登录用户访问登录/注册页，重定向到对应首页
    next(`/${userStore.role.toLowerCase()}`)
  } else {
    next()
  }
})

export default router


