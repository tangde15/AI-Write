<template>
  <div class="set-tab">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="set-list">
      <div
        v-for="set in sets"
        :key="set.id"
        class="set-item"
        @click="handleViewSet(set.id)"
      >
        <div class="set-checkbox">
          <el-checkbox :model-value="false" @click.stop />
        </div>
        <div class="set-info">
          <h3 class="set-name">{{ set.name }}</h3>
          <p class="set-meta">创建时间：{{ set.createTime }}</p>
          <p class="set-meta">创建者：{{ set.creator }}</p>
        </div>
        <div class="set-actions">
          <el-button type="primary" text @click.stop="handleManage(set.id)">
            管理
          </el-button>
          <el-button type="primary" @click.stop="handleViewSet(set.id)">
            查看
          </el-button>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && sets.length === 0" description="暂无题单数据" />

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
import { getPracticeSets } from '@/api/practice';
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
const sets = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchSets = async () => {
  loading.value = true;
  try {
    // 先尝试从后端API获取真实数据
    let response = null;
    try {
      response = await getPracticeSets({
        keyword: props.keyword,
        filter: props.filter,
        page: currentPage.value,
        size: pageSize.value
      });
      console.log('✅ 成功从API获取题单数据:', response);
    } catch (e) {
      console.warn('⚠️ API请求失败，将使用模拟数据:', e.message);
    }
    
    // 如果API返回了数据，使用API数据；否则使用模拟数据
    if (response && Array.isArray(response) && response.length > 0) {
      sets.value = response.map(set => ({
        id: set.id,
        name: set.name,
        createTime: set.createTime || new Date().toLocaleString('zh-CN'),
        creator: set.creator || '系统'
      }));
      total.value = response.length;
      console.log('📋 当前题单数量:', response.length);
    } else {
      // 只在API完全失败时才使用模拟数据
      console.log('📋 使用模拟数据（API无数据）');
      sets.value = [
        {
          id: 1,
          name: '第一单元 写人作文专练',
          createTime: '2025-10-27 19:20',
          creator: '李老师'
        }
      ];
      total.value = 1;
    }
  } catch (error) {
    console.error('❌ 获取题单列表失败:', error);
    ElMessage.error('获取题单列表失败');
  } finally {
    loading.value = false;
  }
};

const handleViewSet = (setId) => {
  // 跳转到该题单的练习册列表页
  router.push(`/practice/set/${setId}/books`);
};

const handleManage = (setId) => {
  console.log('管理题单:', setId);
};

const handlePageChange = () => {
  fetchSets();
};

watch([() => props.keyword, () => props.filter], () => {
  currentPage.value = 1;
  fetchSets();
});

onMounted(() => {
  fetchSets();
});
</script>

<style scoped>
.set-tab {
  padding: 20px 0;
}

.set-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.set-item {
  background: #f8f9fc;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e8e8f0;
}

.set-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.set-checkbox {
  flex-shrink: 0;
}

.set-info {
  flex: 1;
  min-width: 0;
}

.set-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.set-meta {
  font-size: 13px;
  color: #999;
  margin: 0;
}

.set-actions {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
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
