<template>
  <div class="parent-home">
    <!-- 左侧导航栏 -->
    <div class="left-navbar">
      <div class="nav-header">
        <el-avatar>{{ userStore.username.charAt(0) }}</el-avatar>
        <span>{{ userStore.username }}</span>
      </div>
      
      <el-menu
        :default-active="activeNav"
        class="nav-menu"
        background-color="#2c3e50"
        text-color="#ecf0f1"
        active-text-color="#3498db"
      >
        <el-menu-item index="home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-menu-item index="messages" @click="goToChat">
          <el-icon><ChatDotRound /></el-icon>
          <span>聊天</span>
          <el-badge :value="unreadCount" v-if="unreadCount > 0" class="nav-badge" />
        </el-menu-item>
        
        <el-menu-item index="other" @click="showComingSoon">
          <el-icon><Setting /></el-icon>
          <span>其他功能</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 右侧内容区 -->
    <div class="content-area">
      <el-row :gutter="20">
      <!-- 左侧：孩子列表 -->
      <el-col :xs="24" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header-flex">
              <span><el-icon><User /></el-icon> 我的孩子</span>
              <el-button 
                type="primary" 
                size="small"
                :icon="Plus"
                @click="showBindDialog = true"
              >
                绑定孩子
              </el-button>
            </div>
          </template>
          
          <el-list v-if="children.length > 0">
            <el-list-item
              v-for="child in children"
              :key="child.id"
              :class="{ 'active': selectedChild?.id === child.id }"
              @click="selectChild(child)"
              class="child-item"
            >
              <div class="child-info">
                <el-avatar :size="50" :style="{ background: getAvatarColor(child.id) }">
                  {{ child.username.charAt(0) }}
                </el-avatar>
                <div class="child-details">
                  <div class="child-name">{{ child.username }}</div>
                  <div class="child-stats">
                    <el-tag size="small" type="success">
                      作文 {{ child.writingCount || 0 }} 篇
                    </el-tag>
                  </div>
                </div>
              </div>
              <el-icon><ArrowRight /></el-icon>
            </el-list-item>
          </el-list>

          <el-empty v-else description="暂无关联的孩子" />
        </el-card>
      </el-col>

      <!-- 右侧：孩子详情 -->
      <el-col :xs="24" :lg="16">
        <div v-if="!selectedChild" class="empty-state">
          <el-empty description="请选择孩子查看成长记录">
            <el-icon :size="100" color="#909399"><Reading /></el-icon>
          </el-empty>
        </div>

        <div v-else>
          <!-- 成长曲线 -->
          <el-card class="growth-card">
            <template #header>
              <div class="card-header-flex">
                <span>
                  <el-icon><TrendCharts /></el-icon> 
                  {{ selectedChild.username }} 的成长曲线
                </span>
                <el-tag type="success" effect="dark">
                  {{ getGrowthStatus() }}
                </el-tag>
              </div>
            </template>
            <ChartProgress :data="childProgress" />
          </el-card>

          <!-- 写作记录 -->
          <el-card style="margin-top: 20px;">
            <template #header>
              <span><el-icon><Document /></el-icon> 写作记录</span>
            </template>

            <el-timeline v-if="childWritings.length > 0">
              <el-timeline-item
                v-for="writing in childWritings"
                :key="writing.id"
                :timestamp="formatDateTime(writing.createdAt)"
                placement="top"
              >
                <el-card shadow="hover" class="writing-card">
                  <div class="writing-header">
                    <h3>{{ writing.topic }}</h3>
                    <el-tag v-if="writing.teacherFeedback" type="success" size="small">
                      已批改
                    </el-tag>
                  </div>
                  
                  <el-collapse>
                    <el-collapse-item title="查看作文内容" name="1">
                      <p class="writing-content">{{ writing.essay }}</p>
                    </el-collapse-item>
                    
                    <el-collapse-item title="查看AI反馈" name="2">
                      <p class="ai-feedback">{{ writing.aiResponse || '暂无反馈' }}</p>
                    </el-collapse-item>

                    <el-collapse-item 
                      v-if="writing.teacherFeedback" 
                      title="查看教师批改" 
                      name="3"
                    >
                      <p class="teacher-feedback">{{ writing.teacherFeedback }}</p>
                    </el-collapse-item>
                  </el-collapse>
                </el-card>
              </el-timeline-item>
            </el-timeline>

            <el-empty v-else description="孩子还没有写作记录" />
          </el-card>

          <!-- 激励语区域 -->
          <el-card style="margin-top: 20px;">
            <template #header>
              <div class="card-header-flex">
                <span><el-icon><Star /></el-icon> 激励与鼓励</span>
                <el-button 
                  type="primary" 
                  size="small"
                  :icon="ChatDotRound"
                  @click="showEncouragementDialog = true"
                >
                  发送激励语
                </el-button>
              </div>
            </template>

            <EncouragementList :list="sentEncouragements" />
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- 绑定孩子对话框 -->
    <el-dialog
      v-model="showBindDialog"
      title="绑定孩子"
      width="500px"
    >
      <el-alert
        title="绑定说明"
        type="info"
        :closable="false"
        style="margin-bottom: 15px;"
      >
        请输入您孩子的用户名来建立绑定关系，绑定后您就可以查看孩子的学习进度和发送激励语了。
      </el-alert>
      
      <el-form :model="bindForm" label-width="100px">
        <el-form-item label="孩子用户名" required>
          <el-input
            v-model="bindForm.studentUsername"
            placeholder="请输入孩子的用户名"
            clearable
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showBindDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="bindChild" 
          :loading="bindingChild"
          :icon="Link"
        >
          绑定
        </el-button>
      </template>
    </el-dialog>

    <!-- 发送激励语对话框 -->
    <el-dialog
      v-model="showEncouragementDialog"
      title="给孩子发送激励语"
      width="500px"
    >
      <el-alert
        title="温馨提示"
        type="info"
        :closable="false"
        style="margin-bottom: 15px;"
      >
        给孩子一些鼓励和肯定，会让他们更有动力哦！
      </el-alert>
      
      <el-input
        v-model="encouragementContent"
        type="textarea"
        :rows="6"
        placeholder="例如：宝贝，你今天的作文写得很棒！妈妈看到了你的进步..."
        maxlength="500"
        show-word-limit
      />
      
      <template #footer>
        <el-button @click="showEncouragementDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="sendEncouragement" 
          :loading="sendingEncouragement"
          :icon="Position"
        >
          发送
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { parentAPI } from '@/api/parent'
import { bindingAPI } from '@/api/binding'
import { messageAPI } from '@/api/message'
import { ElMessage } from 'element-plus'
import { 
  User, ArrowRight, TrendCharts, Document, 
  Star, ChatDotRound, Position, Reading, Plus, Link,
  HomeFilled, Setting
} from '@element-plus/icons-vue'
import ChartProgress from '@/components/ChartProgress.vue'
import EncouragementList from '@/pages/Common/EncouragementList.vue'

const router = useRouter()
const userStore = useUserStore()
const activeNav = ref('home')
const unreadCount = ref(0)
const children = ref([])
const selectedChild = ref(null)
const childWritings = ref([])
const childProgress = ref([])
const sentEncouragements = ref([])
const showEncouragementDialog = ref(false)
const encouragementContent = ref('')
const sendingEncouragement = ref(false)
const showBindDialog = ref(false)
const bindingChild = ref(false)
const bindForm = ref({
  studentUsername: ''
})

const selectChild = async (child) => {
  console.log('🔄 切换孩子:', child.username, 'ID:', child.id)
  
  // 先清空之前的数据，避免显示缓存
  childWritings.value = []
  childProgress.value = []
  sentEncouragements.value = []
  
  selectedChild.value = child
  await loadChildData(child.id)
}

const loadChildData = async (childId) => {
  try {
    // 加载孩子的写作记录
    const writings = await parentAPI.getChildWritings(childId)
    childWritings.value = writings || []

    // 加载孩子的成长曲线
    const progress = await parentAPI.getChildProgress(childId)
    childProgress.value = progress || []

    // 加载已发送的激励语
    const encouragements = await parentAPI.getSentEncouragements(childId)
    sentEncouragements.value = encouragements || []
    
    console.log(`📊 孩子 ${childId} 的进度数据:`, progress)
  } catch (error) {
    console.error('加载孩子数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const sendEncouragement = async () => {
  if (!encouragementContent.value.trim()) {
    ElMessage.warning('请输入激励内容')
    return
  }

  sendingEncouragement.value = true
  try {
    await parentAPI.sendEncouragement(
      selectedChild.value.id,
      encouragementContent.value
    )
    ElMessage.success('激励语发送成功！孩子会很开心的~')
    showEncouragementDialog.value = false
    encouragementContent.value = ''
    
    // 刷新激励语列表
    await loadChildData(selectedChild.value.id)
  } catch (error) {
    ElMessage.error('发送失败：' + (error.response?.data?.message || error.message))
  } finally {
    sendingEncouragement.value = false
  }
}

const getGrowthStatus = () => {
  if (childProgress.value.length === 0) return '等待数据'
  const latest = childProgress.value[childProgress.value.length - 1]
  const rate = latest?.improvementRate || 0
  if (rate > 10) return '进步显著 🎉'
  if (rate > 0) return '稳步提升 📈'
  return '继续加油 💪'
}

const getAvatarColor = (id) => {
  const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']
  return colors[id % colors.length]
}

const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const bindChild = async () => {
  if (!bindForm.value.studentUsername.trim()) {
    ElMessage.warning('请输入孩子的用户名')
    return
  }

  bindingChild.value = true
  try {
    await bindingAPI.parentBindChild(bindForm.value.studentUsername)
    ElMessage.success('绑定成功！')
    showBindDialog.value = false
    bindForm.value.studentUsername = ''
    
    // 重新加载孩子列表
    await loadChildren()
  } catch (error) {
    ElMessage.error('绑定失败：' + (error.response?.data?.message || error.message))
  } finally {
    bindingChild.value = false
  }
}

const loadChildren = async () => {
  try {
    const data = await parentAPI.getChildren()
    children.value = data || []
    
    // 自动选择第一个孩子
    if (children.value.length > 0) {
      await selectChild(children.value[0])
    }
  } catch (error) {
    console.error('加载孩子列表失败:', error)
  }
}

const goToChat = () => {
  router.push('/chat')
}

const showComingSoon = () => {
  ElMessage.info('功能开发中，敬请期待！')
}

const loadUnreadCount = async () => {
  try {
    const count = await messageAPI.getUnreadCount()
    unreadCount.value = count || 0
  } catch (error) {
    console.error('加载未读数失败:', error)
  }
}

let pollInterval = null

onMounted(() => {
  loadChildren()
  loadUnreadCount()
  
  // 每30秒更新一次未读数
  pollInterval = setInterval(loadUnreadCount, 30000)
})

onUnmounted(() => {
  if (pollInterval) {
    clearInterval(pollInterval)
  }
})
</script>

<style scoped>
.parent-home {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 左侧导航栏 */
.left-navbar {
  width: 240px;
  background: #2c3e50;
  color: #ecf0f1;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
}

.nav-header {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  background: #34495e;
  border-bottom: 1px solid #2c3e50;
}

.nav-header span {
  font-weight: 600;
  color: #ecf0f1;
}

.nav-menu {
  flex: 1;
  border: none;
}

.nav-badge {
  margin-left: 8px;
}

/* 右侧内容区 */
.content-area {
  flex: 1;
  margin-left: 240px;
  padding: 20px;
  max-width: calc(100% - 240px);
}

.child-item {
  padding: 15px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  border: 1px solid #ebeef5;
}

.child-item:hover {
  background: #f5f7fa;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.child-item.active {
  background: #ecf5ff;
  border-color: #409eff;
}

.child-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.child-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.child-name {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.empty-state {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.card-header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.writing-card {
  margin-bottom: 10px;
}

.writing-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.writing-header h3 {
  margin: 0;
  color: #303133;
}

.writing-content, .ai-feedback, .teacher-feedback {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}

.ai-feedback {
  color: #409eff;
}

.teacher-feedback {
  color: #67c23a;
  font-style: italic;
}

@media (max-width: 768px) {
  .el-col {
    margin-bottom: 20px;
  }
}
</style>





