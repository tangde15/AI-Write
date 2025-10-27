<template>
  <div class="encouragement-list">
    <div v-if="list.length === 0" class="empty-encouragement">
      <el-empty description="暂无激励语">
        <el-icon :size="60" color="#c0c4cc"><ChatLineSquare /></el-icon>
      </el-empty>
    </div>

    <div v-else class="encouragement-items">
      <el-card
        v-for="(item, index) in list"
        :key="item.id"
        class="encouragement-card"
        shadow="hover"
        :class="getRoleClass(item.fromRole)"
      >
        <div class="encouragement-header">
          <div class="sender-info">
            <el-avatar :size="40" :style="{ background: getRoleColor(item.fromRole) }">
              {{ getRoleIcon(item.fromRole) }}
            </el-avatar>
            <div class="sender-details">
              <div class="sender-name">
                <span>{{ getSenderName(item) }}</span>
                <el-tag :type="getRoleType(item.fromRole)" size="small" effect="plain">
                  {{ getRoleText(item.fromRole) }}
                </el-tag>
              </div>
              <div class="send-time">{{ formatTime(item.createdAt) }}</div>
            </div>
          </div>
        </div>

        <div class="encouragement-content">
          <el-icon class="quote-icon"><ChatLineSquare /></el-icon>
          <p>{{ item.content }}</p>
        </div>

        <div class="encouragement-footer">
          <el-button 
            type="text" 
            :icon="Star"
            size="small"
            @click="handleLike(item)"
          >
            点赞 {{ item.likes || 0 }}
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ChatLineSquare, Star } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  list: {
    type: Array,
    default: () => []
  }
})

const getRoleIcon = (role) => {
  const iconMap = {
    TEACHER: '👨‍🏫',
    PARENT: '👨‍👩‍👧',
    AI: '🤖'
  }
  return iconMap[role] || '💬'
}

const getRoleColor = (role) => {
  const colorMap = {
    TEACHER: '#67c23a',
    PARENT: '#e6a23c',
    AI: '#409eff'
  }
  return colorMap[role] || '#909399'
}

const getRoleType = (role) => {
  const typeMap = {
    TEACHER: 'success',
    PARENT: 'warning',
    AI: 'primary'
  }
  return typeMap[role] || 'info'
}

const getRoleText = (role) => {
  const textMap = {
    TEACHER: '教师',
    PARENT: '家长',
    AI: 'AI助手'
  }
  return textMap[role] || '未知'
}

const getRoleClass = (role) => {
  return `role-${role.toLowerCase()}`
}

const getSenderName = (item) => {
  if (item.sender?.username) {
    return item.sender.username
  }
  return item.fromRole === 'AI' ? 'AI助手' : '系统'
}

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now - date
  
  // 小于1分钟
  if (diff < 60000) {
    return '刚刚'
  }
  // 小于1小时
  if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`
  }
  // 小于1天
  if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  }
  // 小于7天
  if (diff < 604800000) {
    return `${Math.floor(diff / 86400000)}天前`
  }
  
  // 超过7天显示具体日期
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const handleLike = (item) => {
  // 这里可以添加点赞的API调用
  ElMessage.success('点赞成功！')
}
</script>

<style scoped>
.encouragement-list {
  width: 100%;
}

.empty-encouragement {
  padding: 40px 20px;
  text-align: center;
}

.encouragement-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.encouragement-card {
  border-radius: 12px;
  transition: all 0.3s;
}

.encouragement-card:hover {
  transform: translateY(-2px);
}

.encouragement-card.role-teacher {
  border-left: 4px solid #67c23a;
}

.encouragement-card.role-parent {
  border-left: 4px solid #e6a23c;
}

.encouragement-card.role-ai {
  border-left: 4px solid #409eff;
}

.encouragement-header {
  margin-bottom: 15px;
}

.sender-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.sender-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.sender-name {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  color: #303133;
}

.send-time {
  font-size: 12px;
  color: #909399;
}

.encouragement-content {
  position: relative;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 10px;
}

.quote-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  color: #dcdfe6;
  opacity: 0.5;
}

.encouragement-content p {
  margin: 0;
  line-height: 1.8;
  color: #606266;
  font-size: 14px;
  white-space: pre-wrap;
  word-break: break-word;
}

.encouragement-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 5px;
  border-top: 1px solid #ebeef5;
}
</style>


























