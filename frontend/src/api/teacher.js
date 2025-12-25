import api from './auth'

export const teacherAPI = {
  // 绑定码
  getBindingCode() { return api.get('/teacher/binding/code') },
  resetBindingCode() { return api.post('/teacher/binding/reset') },
  bindByCode(code) { return api.post('/teacher/bind/by-code', null, { params: { code } }) },

  // 学生列表（已绑定）
  getBoundStudents() { return api.get('/teacher/students') },

  // 学生能力
  getStudentSummary(studentId) { return api.get(`/teacher/student/${studentId}/ability/summary`) },
  getStudentTimeseries(studentId, range) { return api.get(`/teacher/student/${studentId}/ability/timeseries`, { params: { range } }) },

  // 作文重批
  reviewWriting(id, payload) { return api.put(`/teacher/writing/${id}/review`, payload) },

  // 学生作文/练习列表与详情（教师端查看）
  getStudentWritings(studentId) { return api.get(`/writing/list`, { params: { studentId } }) },
  getStudentSubmissions(studentId) { return api.get(`/teacher/student/${studentId}/submissions`) },
  getWritingDetail(id) { return api.get(`/writing/detail/${id}`) },
  getPracticeAnswerDetail(id) { return api.get(`/practice/answers/${id}/detail`) },
  reviewPracticeAnswer(id, payload) { return api.put(`/teacher/practice/${id}/review`, payload) }
}


