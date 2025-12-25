import api from './auth'

export const studentAPI = {
  // 提交作文请求（AI辅助）
  submitWriting(data) {
    return api.post('/writing/process', data)
  },

  // 获取学生自己的作文历史
  getMyWritings() {
    return api.get('/student/writings')
  },

  // 获取学生的写作进度
  getProgress() {
    return api.get('/student/progress')
  },

  // 获取学生收到的激励语
  getEncouragements() {
    return api.get('/student/encouragements')
  },

  // 获取当前学生已绑定的教师列表
  getBoundTeachers() {
    return api.get('/student/bound-teachers')
  }
}
