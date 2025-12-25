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
    path: '/student/ability',
    name: 'StudentAbilityDashboard',
    component: () => import('@/pages/Student/AbilityDashboard.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/bind-teacher',
    name: 'BindTeacher',
    component: () => import('@/pages/Student/BindTeacher.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/teacher',
    name: 'TeacherDashboard',
    component: () => import('@/pages/Teacher/TeacherDashboard.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/practice',
    name: 'TeacherPracticeOverview',
    component: () => import('@/pages/Teacher/PracticeOverview.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/student/:id/ability',
    name: 'TeacherStudentAbility',
    component: () => import('@/pages/Teacher/StudentAbility.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/writing/:id',
    name: 'TeacherWritingDetailReview',
    component: () => import('@/pages/Teacher/WritingDetailReview.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/practice/answer/:id',
    name: 'TeacherPracticeAnswerReview',
    component: () => import('@/pages/Teacher/PracticeAnswerReview.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/books/new',
    name: 'TeacherBookEditor',
    component: () => import('@/pages/Teacher/BookEditor.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/books/edit',
    name: 'TeacherBookEdit',
    component: () => import('@/pages/Teacher/BookEditor.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/books',
    name: 'TeacherMyBooks',
    component: () => import('@/pages/Teacher/MyBooks.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/student/book/:id',
    name: 'StudentBookDetail',
    component: () => import('@/pages/Student/PracticeBookDetail.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/parent',
    name: 'ParentHome',
    component: () => import('@/pages/Parent/ParentHome.vue'),
    meta: { requiresAuth: true, role: 'PARENT' }
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('@/pages/Common/Messages.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/chat',
    name: 'ChatWindow',
    component: () => import('@/pages/Common/ChatWindow.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/sample-essays',
    name: 'SampleEssayList',
    component: () => import('@/pages/Common/SampleEssayList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/sample-essay/:id',
    name: 'SampleEssayDetail',
    component: () => import('@/pages/Common/SampleEssayDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/practice',
    name: 'Practice',
    component: () => import('@/pages/Student/PracticePage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/practice/:type/:id/books',
    name: 'PracticeBookList',
    component: () => import('@/pages/Student/PracticeBookList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/practice/book/:id',
    name: 'PracticeBookDetail',
    component: () => import('@/pages/Student/PracticeBookDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/practice/question/:id',
    name: 'PracticeQuestionDetail',
    component: () => import('@/pages/Student/PracticeQuestionDetail.vue'),
    meta: { requiresAuth: true }
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


