import api from './auth'
import { bindingAPI } from './binding'

export const parentAPI = {
  // 获取家长的孩子列表
  async getChildren() {
    const response = await bindingAPI.getMyBindings()
    return response.children || []
  },

  // 获取孩子的写作记录
  getChildWritings(childId) {
    return api.get(`/parent/child/${childId}/writings`)
  },

  // 获取孩子的成长曲线
  getChildProgress(childId) {
    return api.get(`/parent/child/${childId}/progress`)
  },

  // 发送激励语给孩子
  sendEncouragement(childId, content) {
    return api.post('/encouragement/send', {
      studentId: childId,
      content,
      fromRole: 'PARENT'
    })
  },

  // 查看已发送的激励语
  getSentEncouragements(childId) {
    return api.get(`/parent/child/${childId}/encouragements`)
  }
}


