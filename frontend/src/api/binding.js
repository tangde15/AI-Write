import api from './auth'

export const bindingAPI = {
  // 教师绑定学生
  teacherBindStudent(studentUsername) {
    return api.post('/binding/teacher/bind-student', { studentUsername })
  },

  // 家长绑定孩子
  parentBindChild(studentUsername) {
    return api.post('/binding/parent/bind-child', { studentUsername })
  },

  // 学生绑定教师
  studentBindTeacher(teacherUsername) {
    return api.post('/binding/student/bind-teacher', { teacherUsername })
  },

  // 学生绑定家长
  studentBindParent(parentUsername) {
    return api.post('/binding/student/bind-parent', { parentUsername })
  },

  // 获取我的绑定列表
  getMyBindings() {
    return api.get('/binding/my-bindings')
  }
}













