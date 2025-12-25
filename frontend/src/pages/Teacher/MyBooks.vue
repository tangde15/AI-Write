<template>
  <div class="my-books">
    <div class="header">
      <h3>我的题单</h3>
      <el-button type="primary" @click="goNew">新增题单</el-button>
    </div>
    <el-empty v-if="!loading && books.length === 0" description="暂无题单，点击【新增题单】开始创建" />
    <el-table v-else :data="books" v-loading="loading">
      <el-table-column prop="name" label="题单名称" />
      <el-table-column prop="questionCount" label="题目数量" width="120" />
      <el-table-column prop="pushedCount" label="已推送人数" width="120" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 'DRAFT' ? 'info' : 'success'">{{ row.status === 'DRAFT' ? '草稿' : '已发布' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="edit(row.id)">编辑</el-button>
          <el-button size="small" @click="showRecords(row.id)">推送记录</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="recordsDialog" title="推送记录" width="700px">
      <el-table v-if="selectedRecords.length > 0" :data="selectedRecords" size="small">
        <el-table-column prop="studentName" label="学生名称" />
        <el-table-column prop="pushedAt" label="推送时间" width="160" />
        <el-table-column prop="status" label="完成状态" width="120" />
      </el-table>
      <el-empty v-else description="暂无推送记录" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { teacherBooksAPI } from '@/api/books'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const books = ref([])
const recordsDialog = ref(false)
const selectedRecords = ref([])

const load = async () => {
  loading.value = true
  try {
    const list = await teacherBooksAPI.list()
    books.value = Array.isArray(list) ? list : (list?.data || [])
  } catch (e) {
    console.error(e)
    ElMessage.error('加载题单列表失败')
  } finally { 
    loading.value = false 
  }
}

const goNew = () => router.push('/teacher/books/new')

const edit = (id) => {
  // 支持 /teacher/books/:id 路由
  router.push({ path: '/teacher/books/edit', query: { id } })
}

const showRecords = async (id) => {
  try {
    const records = await teacherBooksAPI.getPushRecords(id)
    selectedRecords.value = Array.isArray(records) ? records : (records?.data || [])
    recordsDialog.value = true
  } catch (e) {
    console.error(e)
    ElMessage.error('加载推送记录失败')
  }
}

const del = (id) => {
  ElMessageBox.confirm('确定删除此题单？删除后无法恢复', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await teacherBooksAPI.delete(id)
      ElMessage.success('已删除')
      load()
    } catch (e) {
      console.error(e)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px }
</style>
