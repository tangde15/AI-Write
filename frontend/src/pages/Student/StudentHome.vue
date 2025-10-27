<template>
  <div class="student-home">
    <el-row :gutter="20">
      <!-- 左侧：作文提交区 -->
      <el-col :xs="24" :lg="14">
        <el-card class="writing-card">
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

      <!-- 右侧：进度与激励 -->
      <el-col :xs="24" :lg="10">
        <!-- 写作进度 -->
        <el-card class="progress-card">
          <template #header>
            <span><el-icon><TrendCharts /></el-icon> 我的成长曲线</span>
          </template>
          <ChartProgress :data="progressData" />
        </el-card>

        <!-- 激励语展示 -->
        <el-card class="encouragement-card" style="margin-top: 20px;">
          <template #header>
            <span><el-icon><Star /></el-icon> 激励语</span>
          </template>
          <EncouragementList :list="encouragements" />
        </el-card>

        <!-- 历史记录 -->
        <el-card class="history-card" style="margin-top: 20px;">
          <template #header>
            <span><el-icon><Document /></el-icon> 我的作文历史</span>
          </template>
          
          <el-collapse v-if="writings.length > 0" accordion class="writing-list">
            <el-collapse-item
              v-for="record in writings"
              :key="record.id"
              :name="record.id"
            >
              <template #title>
                <div class="writing-title">
                  <span class="writing-topic">{{ truncateText(record.topic || '无标题', 20) }}</span>
                  <div class="writing-metas">
                    <el-tag size="small" class="score-tag" v-if="record.score">
                      {{ record.score }}分
                    </el-tag>
                    <el-tag size="small" type="success" class="feedback-tag" v-if="record.teacherFeedback">
                      已批改
                    </el-tag>
                    <span class="timestamp">{{ formatDateTime(record.createdAt) }}</span>
                  </div>
                </div>
              </template>

              <div class="writing-detail">
                <div class="detail-section">
                  <h4 class="section-title">📝 作文内容</h4>
                  <div class="essay-text-wrapper">
                    <pre class="essay-text">{{ record.essay }}</pre>
                  </div>
                </div>

                <el-divider class="detail-divider" />

                <div class="detail-section">
                  <h4 class="section-title">🤖 AI反馈</h4>
                  <div class="ai-feedback" v-html="formatResponse(record.aiResponse || '暂无AI反馈')"></div>
                </div>

                <el-divider class="detail-divider" v-if="record.teacherFeedback" />

                <div class="detail-section" v-if="record.teacherFeedback">
                  <h4 class="section-title">👨‍🏫 教师批改</h4>
                  <pre class="teacher-feedback">{{ record.teacherFeedback }}</pre>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>

          <el-empty v-else description="还没有作文记录，快去写作吧！" />
        </el-card>

        <!-- 绑定管理 -->
        <el-card style="margin-top: 20px;">
          <template #header>
            <div class="card-header-flex">
              <span><el-icon><User /></el-icon> 绑定管理</span>
              <el-button size="small" type="primary" @click="showBindingDialog = true">
                添加绑定
              </el-button>
            </div>
          </template>
          <div>
            <el-tag v-for="teacher in teachers" :key="teacher.id" style="margin: 5px;">
              👨‍🏫 {{ teacher.username }}
            </el-tag>
            <el-tag v-for="parent in parents" :key="parent.id" style="margin: 5px;" type="warning">
              👨‍👩‍👧 {{ parent.username }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 绑定对话框 -->
    <el-dialog v-model="showBindingDialog" title="添加绑定" width="400px">
      <el-form :model="bindingForm" label-width="80px">
        <el-form-item label="绑定类型">
          <el-select v-model="bindingForm.type" placeholder="请选择">
            <el-option label="教师" value="teacher" />
            <el-option label="家长" value="parent" />
          </el-select>
        </el-form-item>
        <el-form-item :label="bindingForm.type === 'teacher' ? '教师用户名' : '家长用户名'">
          <el-input v-model="bindingForm.username" placeholder="请输入用户名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBindingDialog = false">取消</el-button>
        <el-button type="primary" @click="submitBinding" :loading="bindingLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { studentAPI } from '@/api/student'
import { bindingAPI } from '@/api/binding'
import { ElMessage } from 'element-plus'
import { 
  Edit, Position, MagicStick, ChatDotRound, 
  TrendCharts, Star, Document, User 
} from '@element-plus/icons-vue'
import ChartProgress from '@/components/ChartProgress.vue'
import EncouragementList from '@/pages/Common/EncouragementList.vue'

const activeTab = ref('write')
const submitting = ref(false)
const gettingInspiration = ref(false)
const aiResponse = ref('')
const progressData = ref([])
const encouragements = ref([])
const writings = ref([])

// 绑定相关
const showBindingDialog = ref(false)
const bindingLoading = ref(false)
const teachers = ref([])
const parents = ref([])

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

const bindingForm = reactive({
  type: 'teacher',
  username: ''
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

// 加载绑定信息
const loadBindings = async () => {
  try {
    const data = await bindingAPI.getMyBindings()
    teachers.value = data.teachers || []
    parents.value = data.parents || []
  } catch (error) {
    console.error('加载绑定信息失败:', error)
  }
}

// 提交绑定
const submitBinding = async () => {
  if (!bindingForm.username.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }

  bindingLoading.value = true
  try {
    if (bindingForm.type === 'teacher') {
      await bindingAPI.studentBindTeacher(bindingForm.username)
    } else {
      await bindingAPI.studentBindParent(bindingForm.username)
    }
    ElMessage.success('绑定成功！')
    showBindingDialog.value = false
    bindingForm.username = ''
    await loadBindings()
  } catch (error) {
    ElMessage.error(error.response?.data || '绑定失败')
  } finally {
    bindingLoading.value = false
  }
}

onMounted(() => {
  loadWritings()
  loadProgress()
  loadEncouragements()
  loadBindings()
})
</script>

<style scoped>
.student-home {
  max-width: 1400px;
  margin: 0 auto;
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

/* 响应式优化 */
@media (max-width: 768px) {
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


