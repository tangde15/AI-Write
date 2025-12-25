<template>
  <div class="pushed-tab">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="4" animated />
    </div>
    <div v-else>
      <el-empty v-if="books.length === 0" description="暂无老师推送的题单" />
      <div v-else class="book-list">
        <div v-for="b in books" :key="b.bookId" class="book-item">
          <div class="book-info">
            <h3 class="book-name">{{ b.name }}</h3>
            <p class="book-meta">推送时间：{{ formatTime(b.pushedAt) }}</p>
            <el-tag :type="statusTagType(b.status)">{{ statusText(b.status) }}</el-tag>
          </div>
          <div class="book-actions">
            <el-button type="primary" @click="openBook(b.bookId)">查看</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { studentBooksAPI } from '@/api/books'

const router = useRouter()
const loading = ref(false)
const books = ref([])

const load = async () => {
  loading.value = true
  try {
    const resp = await studentBooksAPI.pushed()
    books.value = Array.isArray(resp) ? resp : (resp?.data || [])
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const openBook = (id) => {
  router.push(`/student/book/${id}`)
}

const formatTime = (t) => {
  if (!t) return ''
  try {
    return new Date(t).toLocaleString('zh-CN')
  } catch { return String(t) }
}

const statusTagType = (s) => {
  switch (s) {
    case 'COMPLETED': return 'success'
    case 'IN_PROGRESS': return 'warning'
    default: return 'info'
  }
}

const statusText = (s) => {
  switch (s) {
    case 'COMPLETED': return '已完成'
    case 'IN_PROGRESS': return '进行中'
    default: return '待开始'
  }
}

onMounted(load)
</script>

<style scoped>
.book-list { display: flex; flex-direction: column; gap: 12px }
.book-item { display: flex; justify-content: space-between; align-items: center; padding: 14px; border: 1px solid #eee; border-radius: 8px; background: #fafafa }
.book-name { margin: 0 0 6px 0; font-size: 16px; font-weight: 600 }
.book-meta { margin: 0 8px 0 0; font-size: 13px; color: #777 }
.book-actions { display: flex; gap: 8px }
</style>
