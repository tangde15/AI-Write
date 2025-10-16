import api from './auth'
import { bindingAPI } from './binding'

export const teacherAPI = {
  // 获取所有学生列表
  async getStudents() {
    const response = await bindingAPI.getMyBindings()
    return response.students || []
  },

  // 获取某个学生的所有作文
  getStudentWritings(studentId) {
    return api.get(`/teacher/students/${studentId}/records`)
  },

  // 提交教师批改反馈
  submitFeedback(writingId, feedback) {
    return api.post(`/teacher/feedback/${writingId}`, { feedback })
  },

  // 获取学生进步统计
  getStudentProgress(studentId) {
    return api.get(`/teacher/students/${studentId}/progress`)
  },

  // 发送激励语给学生
  sendEncouragement(studentId, content) {
    return api.post('/encouragement/send', {
      studentId,
      content,
      fromRole: 'TEACHER'
    })
  }
}


