<template>
  <div class="book-list-page">
    <div class="page-header">
      <el-button
        :icon="ArrowLeft"
        circle
        class="back-btn"
        @click="handleBack"
      />
      <h1 class="page-title">{{ pageTitle }}</h1>
    </div>

    <div class="page-content">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>
      <div v-else class="books-grid">
        <div
          v-for="book in books"
          :key="book.id"
          class="book-card"
          @click="handleViewBook(book.id)"
        >
          <div class="book-header">
            <h3 class="book-title">{{ book.name }}</h3>
          </div>
          <div class="book-stats">
            <el-tag type="info" size="small">{{ book.questionCount || 0 }} 道题目</el-tag>
            <el-tag :type="book.completedCount > 0 ? 'success' : ''" size="small">
              已完成 {{ book.completedCount || 0 }}/{{ book.questionCount || 0 }}
            </el-tag>
          </div>
          <div class="book-footer">
            <span class="creator">创建者：{{ book.creator || '系统' }}</span>
            <span class="date">{{ formatDate(book.createdAt) }}</span>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && books.length === 0" description="该分类下暂无练习册" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import { getPracticeLibraryBooks, getPracticeSetBooks } from '@/api/practice';
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const books = ref([]);
const containerInfo = ref({
  type: '', // 'library' or 'set'
  id: null,
  title: ''
});

const pageTitle = computed(() => {
  return containerInfo.value.title || (containerInfo.value.type === 'library' ? '题库练习册' : '题单练习册');
});

const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

const fetchBooks = async () => {
  loading.value = true;
  try {
    const type = route.params.type; // 'library' or 'set'
    const id = route.params.id;
    
    containerInfo.value.type = type;
    containerInfo.value.id = id;

    let response;
    if (type === 'library') {
      response = await getPracticeLibraryBooks(id);
      containerInfo.value.title = response.libraryTitle || '题库练习册';
    } else if (type === 'set') {
      response = await getPracticeSetBooks(id);
      containerInfo.value.title = response.setName || '题单练习册';
    }

    if (response && response.books) {
      books.value = response.books;
    }
  } catch (error) {
    console.error('获取练习册列表失败:', error);
    ElMessage.error('获取练习册列表失败');
  } finally {
    loading.value = false;
  }
};

const handleViewBook = (bookId) => {
  router.push(`/practice/book/${bookId}`);
};

const handleBack = () => {
  router.back();
};

onMounted(() => {
  fetchBooks();
});
</script>

<style scoped>
.book-list-page {
  min-height: 100vh;
  background: #f5f5f9;
  padding: 20px;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.page-title {
  color: white;
  font-size: 24px;
  margin: 0;
}

.page-content {
  max-width: 1200px;
  margin: 0 auto;
}

.loading-container {
  padding: 20px;
  background: white;
  border-radius: 8px;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.book-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.book-header {
  margin-bottom: 12px;
}

.book-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.book-stats {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.creator, .date {
  font-size: 12px;
}
</style>
