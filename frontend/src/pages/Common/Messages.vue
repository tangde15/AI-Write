<template>
  <div class="messages-page">
    <!-- 左侧导航栏 -->
    <div class="left-navbar">
      <div class="nav-header">
        <el-avatar>{{ userStore.username.charAt(0) }}</el-avatar>
        <span>{{ userStore.username }}</span>
      </div>
      
      <div class="nav-search">
        <el-input v-model="searchQuery" placeholder="搜索" clearable>
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
      
      <el-menu
        :default-active="activeNav"
        class="nav-menu"
        background-color="#2c3e50"
        text-color="#ecf0f1"
        active-text-color="#3498db"
      >
        <el-menu-item index="home" @click="goToHome">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-menu-item index="messages">
          <el-icon><ChatDotRound /></el-icon>
          <span>消息</span>
          <el-badge :value="unreadCount" v-if="unreadCount > 0" class="nav-badge" />
        </el-menu-item>
        
        <el-menu-item index="other" @click="showComingSoon">
          <el-icon><Setting /></el-icon>
          <span>其他功能</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 中间消息列表 -->
    <div class="middle-list">
      <div class="list-header">
        <el-input v-model="searchQuery" placeholder="搜索对话" clearable>
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>
      <div class="list-tip">
        <p>👥 只显示已绑定的联系人</p>
      </div>
      
      <div class="message-list">
        <div
          v-for="conversation in filteredConversations"
          :key="conversation.partnerId"
          class="message-item"
          :class="{ active: selectedConversation?.partnerId === conversation.partnerId }"
          @click="selectConversation(conversation)"
        >
          <div class="message-avatar">
            {{ conversation.partnerName.charAt(0) }}
          </div>
          <div class="message-content">
            <div class="message-title">
              <span>{{ conversation.partnerName }}</span>
              <el-badge :value="conversation.unreadCount" v-if="conversation.unreadCount > 0" />
            </div>
            <div class="message-preview">
              {{ conversation.lastMessage }}
            </div>
          </div>
          <div class="message-time">
            {{ conversation.lastMessageTime }}
          </div>
        </div>
        
        <el-empty v-if="filteredConversations.length === 0" description="暂无绑定联系人" />
      </div>
    </div>
    
    <!-- 右侧聊天窗口 -->
    <div class="right-chat">
      <ChatWindow
        v-if="selectedConversation"
        :conversation="selectedConversation"
        @send="handleSendMessage"
      />
      <EmptyState v-else />
    </div>
    
    <!-- 新建对话对话框 -->
    <el-dialog v-model="showNewChatDialog" title="新建对话" width="400px">
      <el-form :model="newChatForm" label-width="80px">
        <el-form-item label="收信人">
          <el-select v-model="newChatForm.receiverId" filterable placeholder="选择或搜索用户名">
            <el-option
              v-for="user in availableUsers"
              :key="user.id"
              :label="user.username"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容">
          <el-input
            v-model="newChatForm.content"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showNewChatDialog = false">取消</el-button>
        <el-button type="primary" @click="handleNewChat">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { messageAPI } from '@/api/message'
import { bindingAPI } from '@/api/binding'
import { ElMessage } from 'element-plus'
import { HomeFilled, ChatDotRound, Setting, Search, UserFilled, Message, Plus } from '@element-plus/icons-vue'
import ChatWindow from '@/components/ChatWindow.vue'
import EmptyState from '@/components/EmptyState.vue'

const router = useRouter()
const userStore = useUserStore()

const activeNav = ref('messages')
const searchQuery = ref('')
const unreadCount = ref(0)
const conversations = ref([])
const selectedConversation = ref(null)
const showNewChatDialog = ref(false)
const newChatForm = ref({ receiverId: null, content: '' })
const boundContacts = ref([])

// 过滤对话列表
const filteredConversations = computed(() => {
  let result = conversations.value
  
  if (searchQuery.value) {
    result = result.filter(c => 
      c.partnerName.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }
  
  return result
})

// 加载对话列表（从绑定关系获取）
const loadConversations = async () => {
  try {
    // 获取绑定关系
    const bindings = await bindingAPI.getMyBindings()
    
    // 合并所有绑定联系人
    const allContacts = []
    if (bindings.teachers) {
      bindings.teachers.forEach(t => allContacts.push({ id: t.id, username: t.username, role: 'TEACHER' }))
    }
    if (bindings.parents) {
      bindings.parents.forEach(p => allContacts.push({ id: p.id, username: p.username, role: 'PARENT' }))
    }
    // 如果是教师或家长，有学生列表
    if (bindings.students) {
      bindings.students.forEach(s => allContacts.push({ id: s.id, username: s.username, role: 'STUDENT' }))
    }
    
    // 对于每个联系人，获取对话信息
    const conversationsList = []
    for (const contact of allContacts) {
      try {
        const messages = await messageAPI.getConversation(contact.id)
        const lastMsg = messages && messages.length > 0 ? messages[messages.length - 1] : null
        const unreadCount = messages ? messages.filter(m => !m.isRead && m.senderId === contact.id).length : 0
        
        conversationsList.push({
          partnerId: contact.id,
          partnerName: contact.username,
          partnerRole: contact.role,
          lastMessage: lastMsg ? lastMsg.content : '暂无消息',
          lastMessageTime: lastMsg ? formatTime(lastMsg.createdAt) : '',
          unreadCount: unreadCount,
          lastSenderId: lastMsg ? lastMsg.senderId : null
        })
      } catch (error) {
        // 如果没有对话，也显示联系人
        conversationsList.push({
          partnerId: contact.id,
          partnerName: contact.username,
          partnerRole: contact.role,
          lastMessage: '暂无消息',
          lastMessageTime: '',
          unreadCount: 0,
          lastSenderId: null
        })
      }
    }
    
    conversations.value = conversationsList
  } catch (error) {
    console.error('加载对话列表失败:', error)
    conversations.value = []
  }
}

// 格式化时间
const formatTime = (timeStr) => {
  const date = new Date(timeStr)
  const now = new Date()
  const hours = Math.floor((now - date) / (1000 * 60 * 60))
  
  if (hours < 1) {
    return Math.floor((now - date) / (1000 * 60)) + '分钟前'
  } else if (hours < 24) {
    return hours + '小时前'
  } else if (hours < 48) {
    return '昨天'
  } else {
    return date.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' })
  }
}

// 加载未读数量
const loadUnreadCount = async () => {
  try {
    const count = await messageAPI.getUnreadCount()
    unreadCount.value = count || 0
  } catch (error) {
    console.error('加载未读数失败:', error)
  }
}

// 选择对话
const selectConversation = async (conversation) => {
  selectedConversation.value = conversation
  
  // 加载对话历史
  if (conversation.partnerId) {
    try {
      const messages = await messageAPI.getConversation(conversation.partnerId)
      conversation.messages = messages
      
      // 标记未读消息为已读
      const unreadMessages = messages.filter(m => !m.isRead && m.senderId !== userStore.userId)
      for (const msg of unreadMessages) {
        await messageAPI.markAsRead(msg.id)
      }
      
      // 刷新列表
      loadUnreadCount()
      loadConversations()
    } catch (error) {
      console.error('加载对话记录失败:', error)
    }
  }
}

// 发送消息
const handleSendMessage = async (content) => {
  if (!selectedConversation.value) return
  
  try {
    await messageAPI.sendMessage(selectedConversation.value.partnerId, content)
    // 刷新对话列表
    loadConversations()
    // 重新加载当前对话
    selectConversation(selectedConversation.value)
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败')
  }
}

// 新建对话
const handleNewChat = async () => {
  if (!newChatForm.value.receiverId || !newChatForm.value.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  try {
    await messageAPI.sendMessage(newChatForm.value.receiverId, newChatForm.value.content)
    ElMessage.success('消息已发送')
    showNewChatDialog.value = false
    newChatForm.value = { receiverId: null, content: '' }
    loadConversations()
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

// 返回首页
const goToHome = () => {
  const role = userStore.role
  if (role === 'STUDENT') router.push('/student')
  else if (role === 'TEACHER') router.push('/teacher')
  else if (role === 'PARENT') router.push('/parent')
}

// 显示功能开发中
const showComingSoon = () => {
  ElMessage.info('功能开发中，敬请期待！')
}

// 轮询更新
let pollInterval = null

onMounted(() => {
  loadConversations()
  loadUnreadCount()
  
  // 每30秒更新一次未读数
  pollInterval = setInterval(() => {
    loadUnreadCount()
    loadConversations()
  }, 30000)
})

onUnmounted(() => {
  if (pollInterval) {
    clearInterval(pollInterval)
  }
})
</script>

<style scoped>
.messages-page {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

/* 左侧导航栏 */
.left-navbar {
  width: 240px;
  background: #2c3e50;
  color: #ecf0f1;
  display: flex;
  flex-direction: column;
}

.nav-header {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  background: #34495e;
}

.nav-search {
  padding: 15px;
}

.nav-menu {
  flex: 1;
  border: none;
}

.nav-badge {
  margin-left: 8px;
}

/* 中间消息列表 */
.middle-list {
  width: 350px;
  background: white;
  border-left: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
}

.list-header {
  padding: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.list-tip {
  padding: 10px 15px;
  background: #f0f9ff;
  border-bottom: 1px solid #e0e0e0;
}

.list-tip p {
  margin: 0;
  font-size: 13px;
  color: #606266;
}

.message-list {
  flex: 1;
  overflow-y: auto;
}

.message-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  gap: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.message-item:hover {
  background: #f9f9f9;
}

.message-item.active {
  background: #e3f2fd;
  border-left: 3px solid #2196f3;
}

.message-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: 600;
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-title {
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.message-preview {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-time {
  font-size: 12px;
  color: #999;
  flex-shrink: 0;
}

/* 右侧聊天窗口 */
.right-chat {
  flex: 1;
  background: white;
  border-left: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
}
</style>



