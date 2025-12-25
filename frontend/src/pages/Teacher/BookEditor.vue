<template>
  <div class="book-editor">
    <el-page-header :content="`${bookId ? '编辑' : '新增'}题单`" @back="() => $router.back()" />
    <div v-if="autoSaveStatus" class="save-status">{{ autoSaveStatus }}</div>
    <el-card class="section">
      <template #header>题单基础信息</template>
      <el-form :model="form" label-width="100px">
        <el-form-item label="题单名称">
          <el-input v-model="form.name" maxlength="20" show-word-limit placeholder="请输入题单名称" @input="onFormChange" />
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="form.gradeTag" placeholder="请选择年级" style="width:200px" @change="onFormChange">
            <el-option v-for="g in grades" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="专题标签">
          <el-select v-model="form.topicTags" multiple placeholder="选择专题" style="width:400px" @change="onFormChange">
            <el-option v-for="t in topics" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="section">
      <template #header>题目管理</template>
      <div class="toolbar">
        <el-button type="primary" @click="openAddDialog">新增自定义题目</el-button>
      </div>
      <el-empty v-if="questions.length === 0" description="暂无题目" />
      <el-table v-else :data="questions" size="small">
        <el-table-column prop="title" label="题目名称" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="removeQuestion(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div class="footer">
      <el-button @click="save">保存题单</el-button>
      <el-button type="primary" @click="openPush" :disabled="!bookId">推送题单</el-button>
    </div>

    <el-dialog v-model="addDialog" title="新增题目" width="600px">
      <el-form :model="newQuestion" label-width="90px">
        <el-form-item label="题目名称"><el-input v-model="newQuestion.title" /></el-form-item>
        <el-form-item label="写作要求"><el-input v-model="newQuestion.requirement" type="textarea" :rows="5" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialog=false">取消</el-button>
        <el-button type="primary" @click="confirmAdd">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="pushDialog" title="推送题单给学生" width="700px">
      <div v-loading="pushLoading" class="push-container">
        <div class="select-bar">
          <el-button size="small" @click="selectAll" :disabled="students.length===0">全选</el-button>
          <el-button size="small" @click="clearSelect">取消全选</el-button>
          <span style="margin-left:20px">已选 {{ selectedStudents.length }} / {{ students.length }} 人</span>
        </div>
        <el-empty v-if="students.length === 0" description="暂无学生绑定" />
        <el-table v-else :data="students" @selection-change="onSelectChange" style="margin-top:12px">
          <el-table-column type="selection" :disabled="row => row.alreadyPushed" width="50" />
          <el-table-column prop="username" label="学生用户名" />
          <el-table-column prop="account" label="账号" />
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag v-if="row.alreadyPushed" type="success">已推送</el-tag>
              <el-tag v-else type="info">未推送</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="pushDialog=false">取消</el-button>
        <el-button type="primary" @click="confirmPush" :loading="pushLoading" :disabled="selectedStudents.length===0">
          推送给 {{ selectedStudents.length }} 人
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { teacherBooksAPI } from '@/api/books'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const grades = ['G1','G2','G3','G4','G5','G6','G7','G8','G9']
const topics = ['写人','写景','五感','叙事','说明']

const form = ref({ name: '', gradeTag: '', topicTags: [] })
const bookId = ref(null)
const autoSaveStatus = ref('')
let saveTimer = null
let autoSaveTimer = null

const questions = ref([])
const addDialog = ref(false)
const pushDialog = ref(false)
const newQuestion = ref({ title: '', requirement: '' })

const students = ref([])
const selectedStudents = ref([])
const pushLoading = ref(false)

const save = async () => {
  try {
    autoSaveStatus.value = '保存中...'
    if (!bookId.value) {
      const resp = await teacherBooksAPI.create({
        name: form.value.name || '新题单',
        gradeTag: form.value.gradeTag,
        topicTags: (form.value.topicTags || []).join(',')
      })
      bookId.value = resp.data?.id || resp.id
      ElMessage.success('已创建题单')
      await loadQuestions()
    }
    autoSaveStatus.value = '已保存'
    setTimeout(() => { autoSaveStatus.value = '' }, 2000)
  } catch (e) {
    console.error(e)
    autoSaveStatus.value = '保存失败'
    ElMessage.error('保存失败')
  }
}

const onFormChange = () => {
  clearTimeout(saveTimer)
  autoSaveStatus.value = ''
  saveTimer = setTimeout(() => {
    if (bookId.value) {
      autoSaveStatus.value = '自动保存中...'
      save().catch(() => {})
    }
  }, 2000)
}

const loadQuestions = async () => {
  if (!bookId.value) return
  try {
    const list = await teacherBooksAPI.questions(bookId.value)
    questions.value = Array.isArray(list) ? list : (list?.data || [])
  } catch (e) { console.error(e) }
}

const openAddDialog = () => { addDialog.value = true }
const confirmAdd = async () => {
  try {
    if (!bookId.value) { await save() }
    await teacherBooksAPI.addQuestion(bookId.value, newQuestion.value)
    addDialog.value = false
    newQuestion.value = { title: '', requirement: '' }
    await loadQuestions()
    ElMessage.success('已添加题目')
  } catch (e) { console.error(e); ElMessage.error('添加失败') }
}

const removeQuestion = async (id) => {
  try {
    await teacherBooksAPI.deleteQuestion(id)
    await loadQuestions()
    ElMessage.success('已删除题目')
  } catch (e) { console.error(e); ElMessage.error('删除失败') }
}

const openPush = async () => {
  if (!bookId.value) {
    ElMessage.warning('请先保存题单')
    return
  }
  pushDialog.value = true
  pushLoading.value = true
  try {
    const list = await teacherBooksAPI.pushPreview(bookId.value)
    students.value = Array.isArray(list) ? list : (list?.data || [])
    selectedStudents.value = []
  } catch (e) {
    console.error(e)
    ElMessage.error('获取学生列表失败')
    pushDialog.value = false
  } finally {
    pushLoading.value = false
  }
}

const selectAll = () => {
  selectedStudents.value = students.value.filter(s => !s.alreadyPushed)
}

const clearSelect = () => {
  selectedStudents.value = []
}

const onSelectChange = (selection) => {
  selectedStudents.value = selection
}

const confirmPush = async () => {
  if (selectedStudents.value.length === 0) {
    ElMessage.warning('请选择要推送的学生')
    return
  }
  pushLoading.value = true
  try {
    const studentIds = selectedStudents.value.map(s => s.id)
    const resp = await teacherBooksAPI.push(bookId.value, studentIds)
    const count = resp.data?.count || studentIds.length
    pushDialog.value = false
    selectedStudents.value = []
    students.value = []
    ElMessage.success(`已向 ${count} 名学生推送题单`)
    await openPush()
  } catch (e) {
    console.error(e)
    ElMessage.error('推送失败')
  } finally {
    pushLoading.value = false
  }
}

onMounted(async () => {
  // 支持两种路由方式：/books/:id 或 /books?id=xxx
  const id = route.params.id || route.query.id
  if (id) {
    bookId.value = parseInt(id)
    await loadBook(bookId.value)
    await loadQuestions()
  }
  
  autoSaveTimer = setInterval(() => {
    if (bookId.value && (form.value.name || form.value.gradeTag)) {
      save().catch(() => {})
    }
  }, 120000)
})

const loadBook = async (id) => {
  try {
    const book = await teacherBooksAPI.getBook(id)
    const data = book?.data || book
    form.value = {
      name: data.name || '',
      gradeTag: data.gradeTag || '',
      topicTags: data.topicTags ? data.topicTags.split(',').filter(t => t) : []
    }
    autoSaveStatus.value = '已加载'
    setTimeout(() => { autoSaveStatus.value = '' }, 2000)
  } catch (e) {
    console.error(e)
    ElMessage.error('加载题单失败')
  }
}

onBeforeUnmount(() => {
  clearTimeout(saveTimer)
  clearInterval(autoSaveTimer)
})
</script>


<style scoped>
.book-editor { padding: 12px }
.save-status { 
  position: fixed; top: 60px; right: 24px; 
  background: #f0f9ff; border: 1px solid #b3e5fc; 
  color: #01579b; padding: 8px 12px; border-radius: 4px; 
  font-size: 12px 
}
.section { margin-top: 12px }
.toolbar { margin-bottom: 12px }
.footer { margin-top: 12px; display: flex; gap: 12px }
.push-container { min-height: 300px }
.select-bar { 
  display: flex; align-items: center; gap: 8px; 
  padding: 12px 0; border-bottom: 1px solid #f0f0f0 
}
</style>

