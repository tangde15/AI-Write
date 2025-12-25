<template>
  <div class="book-detail">
    <el-page-header content="题单详情" @back="() => $router.back()" />
    <el-card v-if="book" class="section">
      <template #header>{{ book.name }}</template>
      <p>年级：{{ book.gradeTag || '-' }} | 专题：{{ book.topicTags || '-' }}</p>
      <el-divider />
      <el-empty v-if="questions.length === 0" description="暂无题目" />
      <el-timeline v-else>
        <el-timeline-item v-for="q in questions" :key="q.id" :timestamp="'题目 ' + q.id">
          <h4>{{ q.title }}</h4>
          <p>{{ q.requirement }}</p>
          <el-button type="primary" @click="$router.push('/practice/question/' + q.id)">开始练习</el-button>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { studentBooksAPI } from '@/api/books'

const route = useRoute()
const book = ref(null)
const questions = ref([])

const load = async () => {
  const id = route.params.id
  const resp = await studentBooksAPI.detail(id)
  const data = resp?.data || resp
  book.value = { id: data.id, name: data.name, gradeTag: data.gradeTag, topicTags: data.topicTags }
  questions.value = data.questions || []
}

onMounted(load)
</script>

<style scoped>
.section { margin-top: 12px }
</style>
