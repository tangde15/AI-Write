import api from './auth'

export const encouragementAPI = {
  // 获取激励语列表（根据角色自动筛选）
  getList(studentId = null) {
    const url = studentId 
      ? `/encouragement/list?studentId=${studentId}`
      : '/encouragement/list'
    return api.get(url)
  },

  // 发送激励语
  send(data) {
    return api.post('/encouragement/send', data)
  },

  // 标记激励语已读
  markAsRead(id) {
    return api.put(`/encouragement/${id}/read`)
  }
}







