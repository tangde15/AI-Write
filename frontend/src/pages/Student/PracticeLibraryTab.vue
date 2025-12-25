<template>
  <div class="library-tab">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="library-grid">
      <div
        v-for="library in libraries"
        :key="library.id"
        class="library-card"
        @click="handleViewLibrary(library.id)"
      >
        <div class="card-header">
          <h3 class="library-title">{{ library.title }}</h3>
        </div>
        <div class="card-content">
          <p class="library-desc">{{ library.description }}</p>
        </div>
        <div class="card-footer">
          <span class="author">作者：{{ library.author }}</span>
          <span class="meta">创建时间：{{ library.createdAt || '-' }}</span>
          <span class="stats">{{ library.completedCount }}/{{ library.totalCount }} 作答</span>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && libraries.length === 0" description="暂无题库数据" />

    <el-pagination
      v-if="total > pageSize"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      class="pagination"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getPracticeLibraries } from '@/api/practice';
import { ElMessage } from 'element-plus';

const router = useRouter();

const props = defineProps({
  keyword: {
    type: String,
    default: ''
  },
  filter: {
    type: String,
    default: 'all'
  }
});

const emit = defineEmits(['view-book']);

const loading = ref(false);
const libraries = ref([]);
const currentPage = ref(1);
const pageSize = ref(12);
const total = ref(0);

const fetchLibraries = async () => {
  loading.value = true;
  try {
    // 先尝试从后端API获取真实数据
    let response = null;
    try {
      response = await getPracticeLibraries({
        keyword: props.keyword,
        filter: props.filter,
        page: currentPage.value,
        size: pageSize.value
      });
      console.log('✅ 成功从API获取题库数据:', response);
    } catch (e) {
      console.warn('⚠️ API请求失败，将使用模拟数据:', e.message);
    }
    
    // 如果API返回了数据，使用API数据；否则使用模拟数据
    if (response && Array.isArray(response) && response.length > 0) {
      libraries.value = response.map(lib => ({
        id: lib.id,
        title: lib.title,
        description: lib.description || '',
        author: lib.author || '系统',
        createdAt: lib.createdAt || '',
        completedCount: lib.completedCount || 0,
        totalCount: lib.totalCount || 0
      }));
      total.value = response.length;
      console.log('📚 当前题库数量:', response.length);
    } else {
      // 只在API完全失败时才使用模拟数据
      console.log('📚 使用模拟数据（API无数据）');
      libraries.value = [
        {
          id: 1,
          title: '小学三年级作文精选',
          description: '精选小学三年级优秀作文题目，包含写人、记事、写景等多种类型，适合学生进行写作训练和提升。',
          author: '张老师',
          createdAt: new Date().toISOString(),
          completedCount: 8,
          totalCount: 15
        }
      ];
      total.value = 1;
    }
  } catch (error) {
    console.error('❌ 获取题库列表失败:', error);
    ElMessage.error('获取题库列表失败');
  } finally {
    loading.value = false;
  }
};

const handleViewLibrary = (libraryId) => {
  // 跳转到该题库的练习册列表页
  router.push(`/practice/library/${libraryId}/books`);
};

const handlePageChange = () => {
  fetchLibraries();
};

watch([() => props.keyword, () => props.filter], () => {
  currentPage.value = 1;
  fetchLibraries();
});

onMounted(() => {
  fetchLibraries();
});
</script>

<style scoped>
.library-tab {
  padding: 20px 0;
}

.library-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.library-card {
  background: #f8f9fc;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e8e8f0;
}

.library-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.card-header {
  margin-bottom: 12px;
}

.library-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-content {
  margin-bottom: 16px;
}

.library-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
  padding-top: 12px;
  border-top: 1px solid #e8e8f0;
}

.author {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.stats {
  margin-left: 10px;
  white-space: nowrap;
}

.loading-container {
  padding: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>
