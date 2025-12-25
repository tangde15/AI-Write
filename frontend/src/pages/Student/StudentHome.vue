<template>
  <div class="student-home">
    <!-- 左侧导航栏 -->
    <div class="left-navbar">
      <div class="nav-header">
        <el-avatar>{{ userStore.username.charAt(0) }}</el-avatar>
        <div class="user-info">
          <div class="username">{{ userStore.username }}</div>
          <a href="#" @click.prevent="handleLogout" class="logout-link">退出登录</a>
        </div>
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
          <span>推荐</span>
        </el-menu-item>
        <el-menu-item index="ability" @click="goToAbility">
          <el-icon><Star /></el-icon>
          <span>个人能力</span>
        </el-menu-item>

        <el-menu-item index="bind-teacher" @click="goToBindTeacher">
          <el-icon><Edit /></el-icon>
          <span>绑定教师</span>
        </el-menu-item>
        
      <el-menu-item index="practice" @click="goToPractice">
        <el-icon><Edit /></el-icon>
        <span>练习</span>
      </el-menu-item>
      
      <el-menu-item index="messages" @click="goToChat">
        <el-icon><ChatDotRound /></el-icon>
        <span>聊天</span>
      </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 右侧内容区 -->
    <div class="content-area">
      <el-row :gutter="20">
      <!-- 每日推荐区域 -->
      <el-col :span="24">
        <div class="section-title">每日推荐</div>
        <el-row :gutter="20" style="margin-bottom: 40px;" v-loading="loadingEssays">
          <el-col :span="6" v-for="essay in dailyRecommendations" :key="essay.id" class="recommendation-card-col">
            <el-card class="recommendation-card" shadow="hover" @click="goToEssayDetail(essay.id)">
              <div class="card-content">
                <h3 class="card-title">{{ essay.title }}</h3>
                <p class="card-author">{{ essay.author }}</p>
                <p class="card-preview">{{ getPreview(essay.content) }}</p>
                <div class="card-footer">
                  <el-tag v-if="essay.tag" size="small">{{ essay.tag }}</el-tag>
                  <span class="card-rating">
                    <el-icon><Star /></el-icon>
                    {{ essay.favoriteCount || 0 }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24" v-if="!loadingEssays && dailyRecommendations.length === 0">
            <el-empty description="暂无推荐范文" :image-size="100" />
          </el-col>
        </el-row>
      </el-col>
      
      <!-- 收藏榜单区域 -->
      <el-col :span="24">
        <div class="section-title">收藏榜单</div>
        <el-row :gutter="20" v-loading="loadingFavorites">
          <el-col :span="6" v-for="essay in favoriteList" :key="essay.id" class="recommendation-card-col">
            <el-card class="recommendation-card" shadow="hover" @click="goToEssayDetail(essay.id)">
              <div class="card-content">
                <h3 class="card-title">{{ essay.title }}</h3>
                <p class="card-author">{{ essay.author }}</p>
                <p class="card-preview">{{ getPreview(essay.content) }}</p>
                <div class="card-footer">
                  <el-tag v-if="essay.tag" size="small">{{ essay.tag }}</el-tag>
                  <span class="card-rating">
                    <el-icon><Star /></el-icon>
                    {{ essay.favoriteCount || 0 }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24" v-if="!loadingFavorites && favoriteList.length === 0">
            <el-empty description="暂无收藏榜单" :image-size="100" />
          </el-col>
        </el-row>
      </el-col>
      
      <!-- 左侧：作文提交区（隐藏） -->
      <el-col :xs="24" :lg="14" style="display: none;">
        <el-card class="writing-card" style="display: none;">
          <template #header>
            <div class="card-header-flex">
              <span><el-icon><Edit /></el-icon> 作文提交与AI辅助</span>
            </div>
          </template>

          <el-tabs v-model="activeTab">
            <el-tab-pane label="📝 写作文" name="write">
              <el-form :model="writingForm" label-width="80px">
                <el-form-item label="作文题目">
                  <el-input 
                    v-model="writingForm.topic" 
                    placeholder="例如：我的暑假生活"
                  />
                </el-form-item>

                <el-form-item label="作文内容">
                  <el-input 
                    v-model="writingForm.essay" 
                    type="textarea" 
                    :rows="12"
                    placeholder="请在这里写下你的作文..."
                    maxlength="5000"
                    show-word-limit
                  />
                </el-form-item>

                <el-form-item>
                  <el-switch
                    v-model="enableComparison"
                    active-text="与历史作文对比"
                    inactive-text=""
                  />
                </el-form-item>

                <el-form-item v-if="enableComparison" label="选择对比作文">
                  <el-radio-group v-model="selectedPreviousWriting" class="comparison-radio-group">
                    <el-radio 
                      v-for="record in writings" 
                      :key="record.id" 
                      :label="record.id"
                      class="comparison-radio-item"
                    >
                      <div class="comparison-item-content">
                        <span class="comparison-topic">
                          {{ truncateText(record.topic || '无标题', 30) }}
                        </span>
                        <span class="comparison-meta">
                          {{ formatDateTime(record.createdAt) }}
                          <el-tag size="small" class="comparison-score" v-if="record.score">
                            {{ record.score }}分
                          </el-tag>
                        </span>
                      </div>
                    </el-radio>
                  </el-radio-group>
                  <el-empty v-if="writings.length === 0" description="暂无历史作文" :image-size="80" />
                </el-form-item>

                <el-form-item>
                  <el-button 
                    type="primary" 
                    :loading="submitting"
                    @click="submitWriting"
                    :icon="Position"
                  >
                    提交并获取AI反馈
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="💡 获取灵感" name="inspiration">
              <el-form :model="inspirationForm" label-width="100px">
                <el-form-item label="作文题目">
                  <el-input 
                    v-model="inspirationForm.topic" 
                    placeholder="输入题目，AI帮你生成写作提纲"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button 
                    type="success" 
                    :loading="gettingInspiration"
                    @click="getInspiration"
                    :icon="MagicStick"
                  >
                    生成写作灵感
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>

          <!-- AI反馈区域 -->
          <div v-if="aiResponse" class="ai-response">
            <el-divider content-position="left">
              <el-icon><ChatDotRound /></el-icon> AI反馈
            </el-divider>
            <div class="response-content" v-html="formatResponse(aiResponse)"></div>
          </div>
        </el-card>
      </el-col>

      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { studentAPI } from '@/api/student'
import { sampleEssayAPI } from '@/api/sampleEssay'
import { ElMessage } from 'element-plus'
import { 
  Edit, Position, MagicStick, ChatDotRound, 
  HomeFilled, Star
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const activeNav = ref('home')
const activeTab = ref('write')
const submitting = ref(false)
const gettingInspiration = ref(false)
const aiResponse = ref('')
const progressData = ref([])
const encouragements = ref([])
const writings = ref([])
const dailyRecommendations = ref([])
const favoriteList = ref([])
const loadingEssays = ref(false)
const loadingFavorites = ref(false)


const writingForm = reactive({
  topic: '',
  essay: ''
})

// 对比相关
const enableComparison = ref(false)
const selectedPreviousWriting = ref(null)

const inspirationForm = reactive({
  topic: ''
})

// 提交作文
const submitWriting = async () => {
  if (!writingForm.essay.trim()) {
    ElMessage.warning('请输入作文内容')
    return
  }

  // 如果启用了对比但没有选择历史作文，给出提示
  if (enableComparison.value && !selectedPreviousWriting.value) {
    ElMessage.warning('请选择一篇历史作文进行对比')
    return
  }

  submitting.value = true
  try {
    const requestData = {
      topic: writingForm.topic,
      essay: writingForm.essay
    }

    // 如果启用了对比模式，添加previousWritingId
    if (enableComparison.value && selectedPreviousWriting.value) {
      requestData.previousWritingId = selectedPreviousWriting.value
    }

    const response = await studentAPI.submitWriting(requestData)
    aiResponse.value = response.result || response.content
    ElMessage.success(enableComparison.value ? '提交成功！已生成对比分析反馈' : '提交成功！AI已生成反馈')
    
    // 清空表单
    writingForm.topic = ''
    writingForm.essay = ''
    enableComparison.value = false
    selectedPreviousWriting.value = null
    
    // 刷新历史记录
    loadWritings()
  } catch (error) {
    ElMessage.error('提交失败：' + (error.response?.data?.message || error.message))
  } finally {
    submitting.value = false
  }
}

// 获取写作灵感
const getInspiration = async () => {
  if (!inspirationForm.topic.trim()) {
    ElMessage.warning('请输入作文题目')
    return
  }

  gettingInspiration.value = true
  try {
    const response = await studentAPI.submitWriting({
      topic: inspirationForm.topic
    })
    aiResponse.value = response.result || response.content
    ElMessage.success('灵感已生成！')
  } catch (error) {
    ElMessage.error('获取灵感失败')
  } finally {
    gettingInspiration.value = false
  }
}

// 格式化AI响应
const formatResponse = (text) => {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

// 格式化日期时间
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

// 文本截断处理
const truncateText = (text, maxLength) => {
  if (!text) return '无标题'
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

// 加载数据
const loadWritings = async () => {
  try {
    const data = await studentAPI.getMyWritings()
    writings.value = data || []
  } catch (error) {
    console.error('加载作文历史失败:', error)
  }
}

const loadProgress = async () => {
  try {
    const data = await studentAPI.getProgress()
    progressData.value = data || []
  } catch (error) {
    console.error('加载进度数据失败:', error)
  }
}

const loadEncouragements = async () => {
  try {
    const data = await studentAPI.getEncouragements()
    encouragements.value = data || []
  } catch (error) {
    console.error('加载激励语失败:', error)
  }
}

// 获取内容预览（前100个字符）
const getPreview = (content) => {
  if (!content) return ''
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

// 跳转到范文详情页
const goToEssayDetail = (id) => {
  router.push(`/sample-essay/${id}`)
}

// 加载每日推荐
const loadDailyRecommendations = async () => {
  loadingEssays.value = true
  try {
    console.log('[前端] 开始加载每日推荐...')
    const response = await sampleEssayAPI.getAllEssays()
    console.log('[前端] 每日推荐响应:', response)
    // API拦截器已经返回了response.data，所以response就是数据数组
    dailyRecommendations.value = Array.isArray(response) ? response : []
    console.log('[前端] 每日推荐数据:', dailyRecommendations.value)
  } catch (error) {
    console.error('加载每日推荐失败:', error)
    console.error('错误详情:', error.response || error.message)
    ElMessage.error('加载每日推荐失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingEssays.value = false
  }
}

// 加载收藏榜单
const loadFavoriteList = async () => {
  loadingFavorites.value = true
  try {
    console.log('[前端] 开始加载收藏榜单...')
    const response = await sampleEssayAPI.getTopFavoriteEssays()
    console.log('[前端] 收藏榜单响应:', response)
    // API拦截器已经返回了response.data，所以response就是数据数组
    favoriteList.value = Array.isArray(response) ? response : []
    console.log('[前端] 收藏榜单数据:', favoriteList.value)
  } catch (error) {
    console.error('加载收藏榜单失败:', error)
    console.error('错误详情:', error.response || error.message)
    ElMessage.error('加载收藏榜单失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingFavorites.value = false
  }
}

// 跳转到个人能力
const goToAbility = () => {
  router.push('/student/ability')
}

// 跳转到绑定教师
const goToBindTeacher = () => {
  router.push('/student/bind-teacher')
}


const goToChat = () => {
  router.push('/chat')
}

const goToPractice = () => {
  router.push('/practice')
}

const showComingSoon = () => {
  ElMessage.info('功能开发中，敬请期待！')
}

// 登出功能
const handleLogout = async () => {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('登出失败:', error)
    ElMessage.error('登出失败')
  }
}

onMounted(() => {
  loadWritings()
  loadProgress()
  loadEncouragements()
  loadDailyRecommendations()
  loadFavoriteList()
})

onUnmounted(() => {
  // 清理函数
})
</script>

<style scoped>
.student-home {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 左侧导航栏 */
.left-navbar {
  width: 200px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
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
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  background: rgba(52, 73, 94, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.username {
  font-weight: 600;
  color: #ffffff;
  font-size: 16px;
}

.logout-link {
  color: #87ceeb;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
  cursor: pointer;
}

.logout-link:hover {
  color: #b0e0e6;
  text-decoration: underline;
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

.card-header-flex {
  display: flex;
  align-items: center;
  gap: 8px;
}

.writing-card {
  margin-bottom: 20px;
}

.ai-response {
  margin-top: 30px;
  padding: 20px;
  background: #f0f9ff;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.response-content {
  line-height: 1.8;
  color: #303133;
  white-space: pre-wrap;
}

.essay-preview {
  color: #909399;
  font-size: 13px;
  margin: 5px 0 0 0;
}

.progress-card, .encouragement-card {
  height: auto;
}

.writing-title {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  width: 100%;
  min-height: 40px;
}

.writing-topic {
  flex: 1;
  font-weight: 500;
  font-size: 14px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.writing-metas {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.score-tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-weight: 600;
  min-width: 50px;
  text-align: center;
}

.feedback-tag {
  color: #67c23a;
  border-color: #67c23a;
}

.timestamp {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.history-card {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.writing-list {
  border-radius: 8px;
}

.writing-detail {
  padding: 20px;
  background: #fafbfc;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.section-title {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 15px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 5px;
}

.detail-divider {
  margin: 20px 0 !important;
}

.essay-text-wrapper {
  background: white;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
  max-height: 300px;
  overflow-y: auto;
}

.essay-text {
  margin: 0;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 14px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.ai-feedback {
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #667eea;
  line-height: 1.8;
  color: #303133;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 14px;
}

.teacher-feedback {
  background: #f0f9ff;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #67c23a;
  line-height: 1.8;
  color: #67c23a;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 14px;
  margin: 0;
}

/* 推荐卡片样式 */
.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.recommendation-card-col {
  margin-bottom: 20px;
}

.recommendation-card {
  height: 280px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.recommendation-card:hover {
  transform: translateY(-4px);
}

.card-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 8px 0;
}

.card-author {
  font-size: 13px;
  color: #909399;
  margin: 0 0 12px 0;
}

.card-preview {
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
  margin: 0 0 8px 0;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.card-rating {
  font-size: 13px;
  color: #909399;
}

/* 响应式优化 */
@media (max-width: 1024px) {
  .left-navbar {
    width: 200px;
  }
  
  .content-area {
    margin-left: 200px;
    max-width: calc(100% - 200px);
  }
}

@media (max-width: 768px) {
  .left-navbar {
    position: relative;
    width: 100%;
    min-height: auto;
  }
  
  .content-area {
    margin-left: 0;
    max-width: 100%;
  }
  
  .el-col {
    margin-bottom: 20px;
  }

  .writing-title {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .writing-topic {
    max-width: 100%;
  }

  .writing-metas {
    flex-wrap: wrap;
  }

  .essay-text-wrapper {
    max-height: 200px;
  }
}

/* 对比选择区域样式 */
.comparison-radio-group {
  width: 100%;
}

.comparison-radio-item {
  display: flex !important;
  margin-bottom: 12px;
  padding: 12px 14px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  transition: all 0.2s ease;
  background: white;
  cursor: pointer;
}

/* 覆盖悬停效果 */
.comparison-radio-item:hover {
  border-color: #409eff;
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%) !important;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transform: scale(1.01);
}

/* 选中状态 */
.comparison-radio-item :deep(.el-radio__input.is-checked) ~ .el-radio__label {
  color: #303133;
}

.comparison-radio-item :deep(.el-radio__input.is-checked) ~ .el-radio__label .comparison-item-content {
  opacity: 1;
}

/* 确保radio圆圈与内容对齐 */
.comparison-radio-item :deep(.el-radio__label) {
  width: 100%;
  padding-left: 8px;
  display: flex;
  align-items: center;
  color: #303133 !important;
}

.comparison-radio-item :deep(.el-radio__input) {
  flex-shrink: 0;
  margin-top: 0;
}

.comparison-item-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex: 1;
}

.comparison-topic {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
  color: #303133 !important;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 0;
  display: block;
}

.comparison-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.comparison-score {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-weight: 600;
}
</style>


