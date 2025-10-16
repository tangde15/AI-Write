<template>
  <div class="teacher-dashboard">
    <el-row :gutter="20">
      <!-- 左侧：学生列表 -->
      <el-col :xs="24" :lg="8">
        <el-card>
          <template #header>
            <div class="card-header-flex">
              <span><el-icon><User /></el-icon> 学生列表</span>
              <el-button 
                type="primary" 
                size="small"
                :icon="Plus"
                @click="showBindDialog = true"
              >
                绑定学生
              </el-button>
            </div>
          </template>
          
          <el-input
            v-model="searchKeyword"
            placeholder="搜索学生..."
            :prefix-icon="Search"
            style="margin-bottom: 15px;"
          />

          <el-list>
            <el-list-item
              v-for="student in filteredStudents"
              :key="student.id"
              :class="{ 'active': selectedStudent?.id === student.id }"
              @click="selectStudent(student)"
              class="student-item"
            >
              <div class="student-info">
                <el-avatar :size="40">{{ student.username.charAt(0) }}</el-avatar>
                <div class="student-details">
                  <div class="student-name">{{ student.username }}</div>
                  <div class="student-stats">作文数: {{ student.writingCount || 0 }}</div>
                </div>
              </div>
              <el-icon><ArrowRight /></el-icon>
            </el-list-item>
          </el-list>
        </el-card>
      </el-col>

      <!-- 右侧：学生详情 -->
      <el-col :xs="24" :lg="16">
        <div v-if="!selectedStudent" class="empty-state">
          <el-empty description="请选择一个学生查看详情" />
        </div>

        <div v-else>
          <!-- 学生进步统计 -->
          <el-card class="stat-card">
            <template #header>
              <span><el-icon><TrendCharts /></el-icon> {{ selectedStudent.username }} 的成长曲线</span>
            </template>
            <ChartProgress :data="studentProgress" />
          </el-card>

          <!-- 作文列表 -->
          <el-card style="margin-top: 20px;">
            <template #header>
              <div class="card-header-flex">
                <span><el-icon><Document /></el-icon> 作文列表</span>
                <el-button 
                  type="primary" 
                  size="small"
                  :icon="Message"
                  @click="showEncouragementDialog = true"
                >
                  发送激励语
                </el-button>
              </div>
            </template>

            <el-collapse v-model="activeWriting" accordion>
              <el-collapse-item
                v-for="writing in studentWritings"
                :key="writing.id"
                :name="writing.id"
              >
                <template #title>
                  <div class="writing-title">
                    <strong>{{ writing.topic }}</strong>
                    <el-tag size="small" style="margin-left: 10px;">
                      {{ formatDate(writing.createdAt) }}
                    </el-tag>
                  </div>
                </template>

                <div class="writing-content">
                  <h4>作文内容：</h4>
                  <p class="essay-text">{{ writing.essay }}</p>

                  <el-divider />

                  <h4>AI反馈：</h4>
                  <p class="ai-feedback">{{ writing.aiResponse || '暂无AI反馈' }}</p>

                  <el-divider />

                  <h4>教师批改：</h4>
                  <el-input
                    v-model="feedbackForm[writing.id]"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入您的批改意见..."
                  />
                  <el-button
                    type="primary"
                    size="small"
                    style="margin-top: 10px;"
                    @click="submitFeedback(writing.id)"
                    :loading="submittingFeedback[writing.id]"
                  >
                    提交批改
                  </el-button>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- 绑定学生对话框 -->
    <el-dialog
      v-model="showBindDialog"
      title="绑定学生"
      width="500px"
    >
      <el-alert
        title="绑定说明"
        type="info"
        :closable="false"
        style="margin-bottom: 15px;"
      >
        请输入学生的用户名来建立绑定关系，绑定后您就可以查看学生的作文和发送激励语了。
      </el-alert>
      
      <el-form :model="bindForm" label-width="100px">
        <el-form-item label="学生用户名" required>
          <el-input
            v-model="bindForm.studentUsername"
            placeholder="请输入学生的用户名"
            clearable
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showBindDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="bindStudent" 
          :loading="bindingStudent"
          :icon="Link"
        >
          绑定
        </el-button>
      </template>
    </el-dialog>

    <!-- 发送激励语对话框 -->
    <el-dialog
      v-model="showEncouragementDialog"
      title="发送激励语"
      width="500px"
    >
      <el-input
        v-model="encouragementContent"
        type="textarea"
        :rows="5"
        placeholder="写下对学生的鼓励话语..."
      />
      <template #footer>
        <el-button @click="showEncouragementDialog = false">取消</el-button>
        <el-button type="primary" @click="sendEncouragement" :loading="sendingEncouragement">
          发送
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { teacherAPI } from '@/api/teacher'
import { bindingAPI } from '@/api/binding'
import { ElMessage } from 'element-plus'
import { 
  User, Search, ArrowRight, TrendCharts, 
  Document, Message, Plus, Link
} from '@element-plus/icons-vue'
import ChartProgress from '@/components/ChartProgress.vue'

const searchKeyword = ref('')
const students = ref([])
const selectedStudent = ref(null)
const studentWritings = ref([])
const studentProgress = ref([])
const activeWriting = ref(null)
const feedbackForm = reactive({})
const submittingFeedback = reactive({})
const showEncouragementDialog = ref(false)
const encouragementContent = ref('')
const sendingEncouragement = ref(false)
const showBindDialog = ref(false)
const bindingStudent = ref(false)
const bindForm = ref({
  studentUsername: ''
})

const filteredStudents = computed(() => {
  if (!searchKeyword.value) return students.value
  return students.value.filter(s => 
    s.username.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const selectStudent = async (student) => {
  console.log('🔄 切换学生:', student.username, 'ID:', student.id)
  
  // 先清空之前的数据，避免显示缓存
  studentWritings.value = []
  studentProgress.value = []
  
  selectedStudent.value = student
  await loadStudentData(student.id)
}

const loadStudentData = async (studentId) => {
  try {
    // 加载学生作文
    const writings = await teacherAPI.getStudentWritings(studentId)
    studentWritings.value = writings || []

    // 初始化反馈表单
    writings?.forEach(w => {
      feedbackForm[w.id] = w.teacherFeedback || ''
    })

    // 加载学生进度
    const progress = await teacherAPI.getStudentProgress(studentId)
    studentProgress.value = progress || []
    
    console.log(`📊 学生 ${studentId} 的进度数据:`, progress)
  } catch (error) {
    console.error('加载学生数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const submitFeedback = async (writingId) => {
  const feedback = feedbackForm[writingId]
  if (!feedback?.trim()) {
    ElMessage.warning('请输入批改意见')
    return
  }

  submittingFeedback[writingId] = true
  try {
    await teacherAPI.submitFeedback(writingId, feedback)
    ElMessage.success('批改提交成功')
  } catch (error) {
    ElMessage.error('提交失败')
  } finally {
    submittingFeedback[writingId] = false
  }
}

const sendEncouragement = async () => {
  if (!encouragementContent.value.trim()) {
    ElMessage.warning('请输入激励内容')
    return
  }

  sendingEncouragement.value = true
  try {
    await teacherAPI.sendEncouragement(
      selectedStudent.value.id,
      encouragementContent.value
    )
    ElMessage.success('激励语发送成功')
    showEncouragementDialog.value = false
    encouragementContent.value = ''
  } catch (error) {
    ElMessage.error('发送失败')
  } finally {
    sendingEncouragement.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const bindStudent = async () => {
  if (!bindForm.value.studentUsername.trim()) {
    ElMessage.warning('请输入学生的用户名')
    return
  }

  bindingStudent.value = true
  try {
    await bindingAPI.teacherBindStudent(bindForm.value.studentUsername)
    ElMessage.success('绑定成功！')
    showBindDialog.value = false
    bindForm.value.studentUsername = ''
    
    // 重新加载学生列表
    await loadStudents()
  } catch (error) {
    ElMessage.error('绑定失败：' + (error.response?.data?.message || error.message))
  } finally {
    bindingStudent.value = false
  }
}

const loadStudents = async () => {
  try {
    const data = await teacherAPI.getStudents()
    students.value = data || []
  } catch (error) {
    console.error('加载学生列表失败:', error)
  }
}

onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.teacher-dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.student-item {
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

.student-item:hover {
  background: #f5f7fa;
  transform: translateX(5px);
}

.student-item.active {
  background: #ecf5ff;
  border-color: #409eff;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.student-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.student-name {
  font-weight: bold;
  color: #303133;
}

.student-stats {
  font-size: 12px;
  color: #909399;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.card-header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.writing-title {
  flex: 1;
}

.writing-content {
  padding: 15px;
}

.essay-text, .ai-feedback {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}

.ai-feedback {
  color: #409eff;
}

@media (max-width: 768px) {
  .el-col {
    margin-bottom: 20px;
  }
}
</style>





