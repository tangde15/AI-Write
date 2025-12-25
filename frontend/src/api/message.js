import api from './auth'

export const messageAPI = {
  // 获取消息列表（对话摘要）
  getConversations() {
    return api.get('/chat/contacts')
  },

  // 获取与某人的对话记录
  getConversation(partnerId) {
    return api.get(`/message/conversation/${partnerId}`)
  },

  // 发送消息
  sendMessage(receiverId, content) {
    return api.post('/message/send', { receiverId, content })
  },

  // 标记消息已读
  markAsRead(messageId) {
    return api.put(`/message/read/${messageId}`)
  },

  // 获取未读消息数
  getUnreadCount() {
    return api.get('/message/unread-count')
  },

  // 获取聊天历史（WebSocket用）
  getChatHistory(otherUserId) {
    return api.get(`/chat/history/${otherUserId}`)
  },

  // 获取聊天联系人列表
  getChatContacts() {
    return api.get('/chat/contacts')
  },

  // 标记会话为已读（清除未读数）
  markConversationAsRead(friendId) {
    return api.post(`/chat/read/${friendId}`)
  }
}


