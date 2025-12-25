import { Client } from '@stomp/stompjs'

/**
 * WebSocket聊天服务
 * 处理实时消息通信
 */
class ChatWebSocketService {
  constructor() {
    this.stompClient = null
    this.isConnected = false
    this.messageCallbacks = []
    this.connectionCallbacks = []
    this.subscriptions = new Map()
  }

  /**
   * 连接WebSocket
   */
  connect(userId, username, role) {
    return new Promise((resolve, reject) => {
      try {
        this.stompClient = new Client({
          brokerURL: 'ws://localhost:8080/ws/chat',
          reconnectDelay: 5000,
          debug: () => {}
        })

        this.stompClient.onConnect = (frame) => {
          console.log('[WebSocket] 已连接:', frame)
          this.isConnected = true

          // 订阅私人消息队列
          const queueSubscription = this.stompClient.subscribe(`/queue/messages/${userId}`, (message) => {
            const msg = JSON.parse(message.body)
            console.log('[WebSocket] 收到消息:', msg)
            this.messageCallbacks.forEach(cb => cb(msg))
          })

          // 订阅在线状态
          const onlineSubscription = this.stompClient.subscribe('/topic/users/online', (message) => {
            const msg = JSON.parse(message.body)
            console.log('[WebSocket] 在线状态更新:', msg)
          })

          this.subscriptions.set('queue', queueSubscription)
          this.subscriptions.set('online', onlineSubscription)

          // 发送上线通知
          this.stompClient.publish({
            destination: '/app/chat/online',
            body: JSON.stringify({
              senderUsername: username,
              senderRole: role
            })
          })

          // 触发连接回调
          this.connectionCallbacks.forEach(cb => cb(true))
          resolve()
        }

        this.stompClient.onStompError = (error) => {
          console.error('[WebSocket] STOMP错误:', error)
          this.isConnected = false
          this.connectionCallbacks.forEach(cb => cb(false))
          reject(error)
        }

        this.stompClient.onWebSocketError = (error) => {
          console.error('[WebSocket] 连接错误:', error)
          this.isConnected = false
          this.connectionCallbacks.forEach(cb => cb(false))
          reject(error)
        }

        this.stompClient.activate()
      } catch (error) {
        console.error('[WebSocket] 连接异常:', error)
        reject(error)
      }
    })
  }

  /**
   * 断开连接
   */
  disconnect() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.deactivate()
      console.log('[WebSocket] 已断开连接')
      this.isConnected = false
    }
  }

  /**
   * 发送消息
   */
  sendMessage(senderUsername, senderRole, receiverUsername, content) {
    if (!this.isConnected) {
      console.error('[WebSocket] 未连接到服务器')
      return
    }

    const message = {
      senderUsername,
      senderRole,
      receiverUsername,
      content,
      messageType: 'chat'
    }

    console.log('[WebSocket] 发送消息:', message)
    this.stompClient.publish({
      destination: '/app/chat/send',
      body: JSON.stringify(message)
    })
  }

  /**
   * 标记消息为已读
   */
  markMessageAsRead(messageId) {
    if (!this.isConnected) {
      console.error('[WebSocket] 未连接到服务器')
      return
    }

    this.stompClient.publish({
      destination: '/app/chat/read',
      body: JSON.stringify(messageId)
    })
  }

  /**
   * 注册消息回调
   */
  onMessage(callback) {
    this.messageCallbacks.push(callback)
  }

  /**
   * 注册连接状态回调
   */
  onConnectionChange(callback) {
    this.connectionCallbacks.push(callback)
  }

  /**
   * 获取连接状态
   */
  getConnectionStatus() {
    return this.isConnected
  }
}

// 导出单例
export default new ChatWebSocketService()
