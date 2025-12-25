<template>
  <div class="chat-window">
    <div class="chat-header">
      <h3>{{ conversation.partnerName }}</h3>
      <span class="partner-role">{{ getRoleText(conversation.partnerRole) }}</span>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="message in conversation.messages"
        :key="message.id"
        class="message-bubble"
        :class="{ 'own-message': message.senderId === currentUserId }"
      >
        <div class="message-content">
          {{ message.content }}
        </div>
        <div class="message-time">
          {{ formatMessageTime(message.createdAt) }}
          <span v-if="message.senderId === currentUserId" class="message-status">
            ✓✓
          </span>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="2"
        placeholder="输入消息..."
        @keydown.enter.prevent="handleSend"
      />
      <el-button type="primary" @click="handleSend">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, watch } from 'vue'

const props = defineProps({
  conversation: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['send'])

const inputMessage = ref('')
const messagesContainer = ref(null)
const currentUserId = ref(JSON.parse(localStorage.getItem('user'))?.id)

// 格式化时间
const formatMessageTime = (timeStr) => {
  const date = new Date(timeStr)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 获取角色文本
const getRoleText = (role) => {
  const roleMap = {
    STUDENT: '学生',
    TEACHER: '教师',
    PARENT: '家长'
  }
  return roleMap[role] || '用户'
}

// 发送消息
const handleSend = () => {
  if (!inputMessage.value.trim()) return
  
  emit('send', inputMessage.value)
  inputMessage.value = ''
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

onMounted(() => {
  scrollToBottom()
})

watch(() => props.conversation.messages, () => {
  scrollToBottom()
})
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
  background: #fafafa;
}

.chat-header h3 {
  margin: 0 0 5px 0;
  color: #333;
}

.partner-role {
  font-size: 13px;
  color: #666;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-bubble {
  display: flex;
  flex-direction: column;
  max-width: 60%;
  animation: slideIn 0.3s ease;
}

.message-bubble.own-message {
  align-self: flex-end;
}

.message-content {
  padding: 12px 16px;
  border-radius: 12px;
  word-wrap: break-word;
  background: #e4e7ed;
  color: #333;
}

.own-message .message-content {
  background: #409eff;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  display: flex;
  gap: 6px;
}

.own-message .message-time {
  flex-direction: row-reverse;
}

.message-status {
  color: #67c23a;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #e0e0e0;
  display: flex;
  gap: 10px;
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
</style>

