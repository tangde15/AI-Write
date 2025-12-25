<template>
  <div class="chat-page">
    <!-- 顶部导航条 -->
    <div class="chat-top-bar">
      <el-button 
        :icon="ArrowLeft" 
        circle 
        @click="goBack"
        class="back-button"
      />
      <div class="top-bar-title">
        <el-icon><ChatDotRound /></el-icon>
        <span>聊天</span>
      </div>
      <div class="top-bar-user">
        <el-avatar size="small">{{ userStore.username ? userStore.username.charAt(0) : '?' }}</el-avatar>
        <span>{{ userStore.username }}</span>
      </div>
    </div>

    <div class="chat-container">
      <!-- 左侧联系人列表 -->
      <div class="contacts-panel">
      <div class="contacts-header">
        <h3>💬 消息</h3>
        <el-button 
          type="primary" 
          size="small" 
          circle 
          :icon="Plus"
          @click="showNewChatDialog = true"
        />
      </div>

      <el-input
        v-model="searchKeyword"
        placeholder="搜索联系人..."
        clearable
        class="search-input"
        :prefix-icon="Search"
      />

      <div class="contacts-list" v-loading="loadingContacts">
        <div
          v-for="contact in filteredContacts"
          :key="contact.id"
          class="contact-item"
          :class="{ active: selectedContact?.id === contact.id }"
          @click="selectContact(contact)"
        >
          <el-avatar size="large">{{ contact.contactName ? contact.contactName.charAt(0) : '?' }}</el-avatar>
          <div class="contact-info">
            <div class="contact-header-row">
              <div class="contact-name">{{ contact.contactName }}</div>
              <div class="contact-time">{{ formatTime(contact.lastMessageTime) }}</div>
            </div>
            <div class="contact-preview">{{ contact.lastMessage }}</div>
          </div>
          <el-badge 
            v-if="contact.unreadCount > 0" 
            :value="contact.unreadCount" 
            class="contact-badge"
          />
        </div>

        <el-empty 
          v-if="contacts.length === 0" 
          description="还没有聊天记录"
          :image-size="100"
        />
      </div>
    </div>

    <!-- 右侧聊天窗口 -->
    <div class="chat-panel">
      <!-- 聊天头部 -->
      <div v-if="selectedContact" class="chat-header">
        <div class="header-info">
          <el-avatar size="large">{{ selectedContact.contactName ? selectedContact.contactName.charAt(0) : '?' }}</el-avatar>
          <div>
            <div class="contact-name">{{ selectedContact.contactName }}</div>
            <div class="contact-status" :class="{ online: selectedContact.isOnline }">
              {{ selectedContact.isOnline ? '在线' : '离线' }}
            </div>
          </div>
        </div>
        <div class="header-actions">
          <el-button :icon="Phone" circle text />
          <el-button :icon="VideoCamera" circle text />
          <el-button :icon="MoreFilled" circle text />
        </div>
      </div>

      <!-- 消息列表 -->
      <div v-if="selectedContact" class="messages-area" ref="messagesContainer">
        <div v-loading="loadingMessages" class="messages-content">
          <div
            v-for="msg in currentChatMessages"
            :key="msg.id"
            class="message-item"
            :class="{ sent: msg.senderId === currentUserId }"
          >
            <el-avatar 
              v-if="msg.senderId !== currentUserId"
              size="small"
              class="message-avatar"
            >
              {{ msg.senderUsername ? msg.senderUsername.charAt(0) : '?' }}
            </el-avatar>

            <div class="message-content">
              <div class="message-bubble">{{ msg.content }}</div>
              <div class="message-time">{{ formatDetailedTime(msg.createdAt) }}</div>
            </div>

            <el-avatar 
              v-if="msg.senderId === currentUserId"
              size="small"
              class="message-avatar"
            >
              {{ msg.senderUsername ? msg.senderUsername.charAt(0) : '?' }}
            </el-avatar>
          </div>
        </div>
      </div>

      <!-- 消息输入区 -->
      <div v-if="selectedContact" class="message-input-area">
        <el-input
          v-model="messageContent"
          type="textarea"
          placeholder="输入消息内容..."
          :rows="3"
          maxlength="500"
          show-word-limit
          @keyup.enter="sendMessage"
        />
        <div class="input-actions">
          <el-button type="primary" @click="sendMessage" :loading="sending">
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
        </div>
      </div>

      <!-- 未选择联系人时的提示 -->
      <div v-else class="empty-state">
        <el-empty description="选择联系人开始聊天" :image-size="150" />
      </div>
    </div>

    <!-- 好友管理对话框 -->
    <el-dialog
      v-model="showNewChatDialog"
      title="好友管理"
      width="600px"
    >
      <el-tabs v-model="friendTabActive">
        <!-- 搜索添加好友 -->
        <el-tab-pane label="添加好友" name="search">
          <el-form :model="friendSearchForm" label-width="100px">
            <el-form-item label="账号搜索" required>
              <el-input
                v-model="friendSearchForm.account"
                placeholder="输入账号名称搜索用户"
                clearable
                @keyup.enter="searchUserByAccount"
              >
                <template #append>
                  <el-button :icon="Search" @click="searchUserByAccount" :loading="searchingUser">搜索</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-form>

          <!-- 搜索结果 -->
          <div v-if="searchedUser" class="search-result">
            <div class="user-card">
              <el-avatar size="large">{{ searchedUser.username ? searchedUser.username.charAt(0) : '?' }}</el-avatar>
              <div class="user-info">
                <div class="user-name">{{ searchedUser.username }}</div>
                <div class="user-role">{{ getRoleText(searchedUser.role) }}</div>
                <div class="user-account">账号: {{ searchedUser.account }}</div>
              </div>
              <el-button 
                v-if="!searchedUser.isFriend && !searchedUser.requestSent"
                type="primary" 
                @click="sendFriendRequest(searchedUser.id)"
                :loading="sendingRequest"
              >
                添加好友
              </el-button>
              <el-tag v-else-if="searchedUser.isFriend" type="success">已是好友</el-tag>
              <el-tag v-else type="info">已发送请求</el-tag>
            </div>
          </div>
          <el-empty v-else-if="searchNoResult" description="未找到该用户" :image-size="80" />
        </el-tab-pane>

        <!-- 好友请求列表 -->
        <el-tab-pane label="好友请求" name="requests">
          <div v-loading="loadingRequests" class="friend-requests">
            <div
              v-for="request in friendRequests"
              :key="request.id"
              class="request-item"
            >
              <el-avatar size="default">{{ request.username ? request.username.charAt(0) : '?' }}</el-avatar>
              <div class="request-info">
                <div class="request-name">{{ request.username }}</div>
                <div class="request-time">{{ formatTime(request.createdAt) }}</div>
              </div>
              <div class="request-actions">
                <el-button 
                  size="small" 
                  type="primary" 
                  @click="acceptFriendRequest(request.id)"
                  :loading="request.accepting"
                >
                  接受
                </el-button>
                <el-button 
                  size="small" 
                  @click="rejectFriendRequest(request.id)"
                  :loading="request.rejecting"
                >
                  拒绝
                </el-button>
              </div>
            </div>
            <el-empty 
              v-if="friendRequests.length === 0 && !loadingRequests" 
              description="暂无好友请求" 
              :image-size="80"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import {
  Plus, Search, Phone, VideoCamera, MoreFilled, Promotion, ArrowLeft, ChatDotRound
} from '@element-plus/icons-vue'
import chatWebSocketService from '@/api/chat'
import { messageAPI } from '@/api/message'
import friendAPI from '@/api/friend'

const router = useRouter()
const userStore = useUserStore()
const currentUserId = ref(userStore.userId)

// 返回上一页
const goBack = () => {
  router.back()
}

// 数据状态
const contacts = ref([])
const selectedContact = ref(null)
const searchKeyword = ref('')
const messageContent = ref('')
const currentChatMessages = ref([])

// UI状态
const loadingContacts = ref(false)
const loadingMessages = ref(false)
const sending = ref(false)
const showNewChatDialog = ref(false)
const messagesContainer = ref(null)

// 好友管理状态
const friendTabActive = ref('search')
const friendSearchForm = ref({ account: '' })
const searchedUser = ref(null)
const searchNoResult = ref(false)
const searchingUser = ref(false)
const sendingRequest = ref(false)
const friendRequests = ref([])
const loadingRequests = ref(false)

/**
 * 格式化时间
 */
const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()

  // 同一天
  if (date.toDateString() === now.toDateString()) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }

  // 昨天
  const yesterday = new Date(now)
  yesterday.setDate(yesterday.getDate() - 1)
  if (date.toDateString() === yesterday.toDateString()) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }

  // 其他日期
  return date.toLocaleDateString('zh-CN')
}

/**
 * 格式化详细时间
 */
const formatDetailedTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit',
    second: '2-digit'
  })
}

/**
 * 获取角色文本
 */
const getRoleText = (role) => {
  const roleMap = {
    STUDENT: '学生',
    TEACHER: '教师',
    PARENT: '家长'
  }
  return roleMap[role] || role
}

/**
 * 过滤联系人
 */
const filteredContacts = computed(() => {
  if (!searchKeyword.value) return contacts.value
  return contacts.value.filter(contact =>
    contact.contactName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

/**
 * 加载联系人列表（结合好友列表和会话信息）
 */
const loadContacts = async () => {
  loadingContacts.value = true
  try {
    console.log('[Chat] 开始加载联系人列表...')
    
    // 1. 先获取好友列表
    const friendResponse = await friendAPI.getFriendList()
    console.log('[Chat] 好友列表API返回:', friendResponse)
    
    if (!friendResponse || !friendResponse.data) {
      console.error('[Chat] API返回数据格式错误:', friendResponse)
      ElMessage.warning('联系人数据格式错误')
      return
    }
    
    const friendList = friendResponse.data
    if (!Array.isArray(friendList)) {
      console.error('[Chat] 好友列表不是数组:', friendList)
      return
    }
    
    if (friendList.length === 0) {
      console.log('[Chat] 暂无好友')
      return
    }
    
    // 2. 尝试获取会话信息（包含最后消息）
    let conversationMap = new Map()
    try {
      const convResponse = await messageAPI.getConversations()
      console.log('[Chat] 会话信息API返回:', convResponse)
      if (Array.isArray(convResponse)) {
        convResponse.forEach(conv => {
          conversationMap.set(conv.id, conv)
        })
      }
    } catch (error) {
      console.log('[Chat] 获取会话信息失败，使用默认值:', error.message)
    }
    
    // 3. 合并好友信息和会话信息
    const mappedContacts = friendList.map(friend => {
      const conversation = conversationMap.get(friend.id)
      return {
        id: friend.id,
        contactName: friend.username || '未知用户',
        account: friend.account || '',
        role: friend.role || '',
        lastMessage: conversation?.lastMessage || '暂无消息',
        lastMessageTime: conversation?.lastMessageTime || null,
        unreadCount: conversation?.unreadCount || 0,
        isOnline: false
      }
    })
    
    // 4. 按最后消息时间排序（有消息的在前）
    contacts.value = mappedContacts.sort((a, b) => {
      if (!a.lastMessageTime) return 1
      if (!b.lastMessageTime) return -1
      return new Date(b.lastMessageTime) - new Date(a.lastMessageTime)
    })
    console.log('[Chat] 联系人列表加载完成:', contacts.value)
  } catch (error) {
    console.error('[Chat] 加载联系人列表失败:', error)
    console.error('[Chat] 错误详情:', error.response || error.message)
    ElMessage.error('加载联系人列表失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingContacts.value = false
  }
}

/**
 * 选择联系人
 */
const selectContact = async (contact) => {
  selectedContact.value = contact
  
  // 清除该联系人的未读消息数（前端和后端同步）
  if (contact.unreadCount > 0) {
    contact.unreadCount = 0
    console.log('[Chat] 已清除未读消息数:', contact.contactName)
    
    // 调用后端API清除数据库中的未读数
    try {
      await messageAPI.markConversationAsRead(contact.id)
      console.log('[Chat] 后端未读数已清除')
    } catch (error) {
      console.error('[Chat] 清除后端未读数失败:', error)
    }
  }
  
  await loadChatMessages(contact.id)
}

/**
 * 加载聊天消息
 */
const loadChatMessages = async (contactId) => {
  loadingMessages.value = true
  try {
    const response = await messageAPI.getChatHistory(contactId)
    // 确保每条消息都有唯一 ID 和必需字段
    currentChatMessages.value = Array.isArray(response) 
      ? response.map(msg => ({
          ...msg,
          id: msg.id || (Date.now() + Math.random()),
          senderUsername: msg.senderUsername || '未知',
          content: msg.content || '',
          createdAt: msg.createdAt || new Date().toISOString()
        }))
      : []
    console.log('[Chat] 聊天历史:', currentChatMessages.value)

    // 滚动到底部
    await nextTick()
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  } catch (error) {
    console.error('[Chat] 加载聊天历史失败:', error)
    ElMessage.error('加载聊天历史失败')
  } finally {
    loadingMessages.value = false
  }
}

/**
 * 发送消息
 */
const sendMessage = async () => {
  if (!messageContent.value.trim()) {
    ElMessage.warning('消息不能为空')
    return
  }

  if (!selectedContact.value) {
    ElMessage.warning('请先选择联系人')
    return
  }

  if (!chatWebSocketService.getConnectionStatus()) {
    ElMessage.error('WebSocket未连接')
    return
  }

  sending.value = true
  try {
    // 通过WebSocket发送消息
    chatWebSocketService.sendMessage(
      userStore.username,
      userStore.role,
      selectedContact.value.contactName,
      messageContent.value
    )

    messageContent.value = ''
    ElMessage.success('消息已发送')
  } catch (error) {
    console.error('[Chat] 发送消息失败:', error)
    ElMessage.error('发送消息失败')
  } finally {
    sending.value = false
  }
}

/**
 * 搜索用户（通过账号）
 */
const searchUserByAccount = async () => {
  if (!friendSearchForm.value.account.trim()) {
    ElMessage.warning('请输入账号名称')
    return
  }

  searchingUser.value = true
  searchedUser.value = null
  searchNoResult.value = false

  try {
    const response = await friendAPI.searchUser(friendSearchForm.value.account.trim())
    
    // 检查是否有错误
    if (response.data.error) {
      ElMessage.error(response.data.message || '搜索失败')
      searchNoResult.value = true
      return
    }
    
    // 检查是否找到用户
    if (response.data.found && response.data.username) {
      searchedUser.value = {
        id: response.data.id,
        username: response.data.username || '',
        account: response.data.account || '',
        role: response.data.role || '',
        isFriend: response.data.isFriend || false,
        requestSent: response.data.requestSent || false
      }
      searchNoResult.value = false
    } else {
      ElMessage.info(response.data.message || '未找到该用户')
      searchNoResult.value = true
    }
  } catch (error) {
    console.error('[Friend] 搜索用户失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '搜索失败'
    ElMessage.error(errorMsg)
    searchNoResult.value = true
  } finally {
    searchingUser.value = false
  }
}

/**
 * 发送好友请求
 */
const sendFriendRequest = async (friendId) => {
  sendingRequest.value = true
  try {
    await friendAPI.sendFriendRequest(friendId)
    ElMessage.success('好友请求已发送')
    if (searchedUser.value) {
      searchedUser.value.requestSent = true
    }
  } catch (error) {
    console.error('[Friend] 发送好友请求失败:', error)
    ElMessage.error(error.response?.data?.message || '发送好友请求失败')
  } finally {
    sendingRequest.value = false
  }
}

/**
 * 加载好友请求列表
 */
const loadFriendRequests = async () => {
  loadingRequests.value = true
  try {
    const response = await friendAPI.getFriendRequests()
    friendRequests.value = (response.data || []).map(req => ({
      id: req.id,
      userId: req.userId,
      username: req.username || '',
      account: req.account || '',
      role: req.role || '',
      createdAt: req.createdAt,
      accepting: false,
      rejecting: false
    }))
  } catch (error) {
    console.error('[Friend] 加载好友请求失败:', error)
    ElMessage.error('加载好友请求失败')
  } finally {
    loadingRequests.value = false
  }
}

/**
 * 接受好友请求
 */
const acceptFriendRequest = async (requestId) => {
  const request = friendRequests.value.find(r => r.id === requestId)
  if (request) request.accepting = true

  try {
    await friendAPI.acceptFriendRequest(requestId)
    ElMessage.success('已接受好友请求')
    loadFriendRequests() // 刷新列表
    loadContacts() // 刷新联系人列表
  } catch (error) {
    console.error('[Friend] 接受好友请求失败:', error)
    ElMessage.error('接受好友请求失败')
  } finally {
    if (request) request.accepting = false
  }
}

/**
 * 拒绝好友请求
 */
const rejectFriendRequest = async (requestId) => {
  const request = friendRequests.value.find(r => r.id === requestId)
  if (request) request.rejecting = true

  try {
    await friendAPI.rejectFriendRequest(requestId)
    ElMessage.success('已拒绝好友请求')
    loadFriendRequests() // 刷新列表
  } catch (error) {
    console.error('[Friend] 拒绝好友请求失败:', error)
    ElMessage.error('拒绝好友请求失败')
  } finally {
    if (request) request.rejecting = false
  }
}

/**
 * 处理WebSocket消息
 */
const handleWebSocketMessage = (message) => {
  console.log('[Chat] WebSocket消息:', message)

  // 确保消息有唯一ID
  if (!message.id) {
    message.id = Date.now() + Math.random()
  }

  // 判断消息是否属于当前对话
  // 情况1：对方发给我的消息（senderId是对方，receiverId是我）
  // 情况2：我发给对方的消息（senderId是我，receiverId是对方）
  const isCurrentConversation = selectedContact.value && (
    (message.senderId === selectedContact.value.id && message.receiverId === currentUserId.value) ||
    (message.senderId === currentUserId.value && message.receiverId === selectedContact.value.id)
  )

  if (isCurrentConversation) {
    // 避免重复添加相同的消息
    const exists = currentChatMessages.value.find(m => m.id === message.id)
    if (!exists) {
      // 使用不可变更新方式
      currentChatMessages.value = [...currentChatMessages.value, message]
      console.log('[Chat] 添加消息到当前对话:', message.id)
    }

    // 自动滚动到底部
    nextTick(() => {
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
      }
    })

    // 如果是对方发来的消息，标记为已读并清除未读数
    if (message.senderId !== currentUserId.value) {
      // 标记单条消息为已读
      if (!message.isRead && message.id) {
        chatWebSocketService.markMessageAsRead(message.id)
      }
      
      // 清除会话的未读数（因为正在查看）
      try {
        messageAPI.markConversationAsRead(message.senderId)
        console.log('[Chat] 已清除会话未读数（WebSocket消息）')
      } catch (error) {
        console.error('[Chat] 清除会话未读数失败:', error)
      }
    }
    
    // 更新当前联系人的最后消息（不增加未读数，因为正在查看）
    if (selectedContact.value) {
      selectedContact.value.lastMessage = message.content
      selectedContact.value.lastMessageTime = message.createdAt
      selectedContact.value.unreadCount = 0  // 确保未读数为0
      // 同步更新联系人列表
      const contact = contacts.value.find(c => c.id === selectedContact.value.id)
      if (contact) {
        contact.lastMessage = message.content
        contact.lastMessageTime = message.createdAt
        contact.unreadCount = 0  // 确保未读数为0
      }
    }
  } else {
    // 更新联系人列表中的最后一条消息
    // 只有当消息不是当前对话时才更新联系人列表
    const otherUserId = message.senderId === currentUserId.value ? message.receiverId : message.senderId
    const contact = contacts.value.find(c => c.id === otherUserId)
    if (contact) {
      contact.lastMessage = message.content
      contact.lastMessageTime = message.createdAt
      // 只有对方发来的消息才增加未读数
      if (message.senderId !== currentUserId.value) {
        contact.unreadCount = (contact.unreadCount || 0) + 1
      }
    }
  }
}

/**
 * 连接WebSocket
 */
const connectWebSocket = async () => {
  try {
    await chatWebSocketService.connect(
      userStore.userId,
      userStore.username,
      userStore.role
    )
    console.log('[Chat] WebSocket已连接')

    // 注册消息回调
    chatWebSocketService.onMessage(handleWebSocketMessage)
  } catch (error) {
    console.error('[Chat] WebSocket连接失败:', error)
    ElMessage.error('连接失败，请检查网络')
  }
}

// 监听好友对话框打开，切换到请求标签时加载数据
watch([showNewChatDialog, friendTabActive], ([dialogOpen, tab]) => {
  if (dialogOpen && tab === 'requests') {
    loadFriendRequests()
  }
})

// 生命周期
onMounted(async () => {
  console.log('[Chat] 组件已挂载')
  await loadContacts()
  await connectWebSocket()
})

onUnmounted(() => {
  console.log('[Chat] 组件已卸载')
  
  // 断开 WebSocket
  chatWebSocketService.disconnect()
  
  // 清理所有响应式数据，避免卸载时的 DOM 操作错误
  contacts.value = []
  currentChatMessages.value = []
  friendRequests.value = []
  selectedContact.value = null
  searchedUser.value = null
})
</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}

/* 顶部导航条 */
.chat-top-bar {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.back-button {
  margin-right: 15px;
}

.top-bar-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  flex: 1;
}

.top-bar-user {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}

.chat-container {
  display: flex;
  flex: 1;
  overflow: hidden;
  background: #f5f5f5;
}

/* 左侧联系人面板 */
.contacts-panel {
  width: 300px;
  background: #fff;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e0e0e0;
}

.contacts-header {
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.contacts-header h3 {
  margin: 0;
  font-size: 18px;
}

.search-input {
  margin: 10px 10px;
}

.contacts-list {
  flex: 1;
  overflow-y: auto;
}

.contact-item {
  padding: 10px 15px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
  transition: background-color 0.3s;
}

.contact-item:hover {
  background-color: #f9f9f9;
}

.contact-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.contact-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}

.contact-name {
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
  flex: 1;
}

.contact-time {
  font-size: 11px;
  color: #999;
  white-space: nowrap;
  flex-shrink: 0;
}

.contact-item.active .contact-time {
  color: rgba(255, 255, 255, 0.7);
}

.contact-preview {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.contact-item.active .contact-preview {
  color: rgba(255, 255, 255, 0.8);
}

.contact-badge {
  margin-left: auto;
}

/* 右侧聊天面板 */
.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.chat-header {
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-info .contact-name {
  font-size: 16px;
  font-weight: 600;
}

.contact-status {
  font-size: 12px;
  color: #999;
}

.contact-status.online {
  color: #67c23a;
}

.header-actions {
  display: flex;
  gap: 10px;
}

/* 消息区域 */
.messages-area {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
}

.messages-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-item {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  animation: slideIn 0.3s ease-in;
}

.message-item.sent {
  flex-direction: row-reverse;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 3px;
  max-width: 60%;
}

.message-item.sent .message-content {
  align-items: flex-end;
}

.message-bubble {
  padding: 10px 15px;
  border-radius: 10px;
  word-break: break-word;
  white-space: pre-wrap;
  line-height: 1.5;
}

.message-item:not(.sent) .message-bubble {
  background: #e9ecef;
  color: #333;
}

.message-item.sent .message-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-item.sent .message-time {
  text-align: right;
}

/* 消息输入区 */
.message-input-area {
  padding: 15px;
  border-top: 1px solid #f0f0f0;
  background: #fff;
}

.message-input-area :deep(.el-textarea) {
  margin-bottom: 10px;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 空状态 */
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

/* 好友管理对话框样式 */
.search-result {
  margin-top: 20px;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: #fafafa;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

.user-role {
  font-size: 14px;
  color: #909399;
  margin-bottom: 3px;
}

.user-account {
  font-size: 12px;
  color: #c0c4cc;
}

.friend-requests {
  max-height: 400px;
  overflow-y: auto;
}

.request-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.request-item:hover {
  background: #fafafa;
}

.request-info {
  flex: 1;
}

.request-name {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 5px;
}

.request-time {
  font-size: 12px;
  color: #909399;
}

.request-actions {
  display: flex;
  gap: 10px;
}

/* 响应式 */
@media (max-width: 768px) {
  .contacts-panel {
    width: 100%;
  }

  .chat-panel {
    display: none;
  }

  .chat-container.show-chat .contacts-panel {
    display: none;
  }

  .chat-container.show-chat .chat-panel {
    display: flex;
    width: 100%;
  }
}
</style>
