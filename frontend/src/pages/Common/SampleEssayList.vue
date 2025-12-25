<template>
  <div class="sample-essay-list">
    <!-- 每日推荐 -->
    <div class="section">
      <h2 class="section-title">每日推荐</h2>
      <div class="essay-grid" v-loading="loading">
        <div 
          v-for="essay in dailyRecommendations" 
          :key="essay.id"
          class="essay-card"
          @click="goToDetail(essay.id)"
        >
          <h3 class="essay-title">{{ essay.title }}</h3>
          <p class="essay-author">{{ essay.author }}</p>
          <p class="essay-preview">{{ getPreview(essay.content) }}</p>
          <div class="essay-footer">
            <el-tag v-if="essay.tag" size="small" class="essay-tag">{{ essay.tag }}</el-tag>
            <div class="essay-favorite">
              <el-icon><Star /></el-icon>
              <span>{{ essay.favoriteCount || 0 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 收藏榜单 -->
    <div class="section">
      <h2 class="section-title">收藏榜单</h2>
      <div class="essay-grid" v-loading="loadingFavorites">
        <div 
          v-for="essay in favoriteList" 
          :key="essay.id"
          class="essay-card"
          @click="goToDetail(essay.id)"
        >
          <h3 class="essay-title">{{ essay.title }}</h3>
          <p class="essay-author">{{ essay.author }}</p>
          <p class="essay-preview">{{ getPreview(essay.content) }}</p>
          <div class="essay-footer">
            <el-tag v-if="essay.tag" size="small" class="essay-tag">{{ essay.tag }}</el-tag>
            <div class="essay-favorite">
              <el-icon><Star /></el-icon>
              <span>{{ essay.favoriteCount || 0 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Star } from '@element-plus/icons-vue'
import { sampleEssayAPI } from '@/api/sampleEssay'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const loadingFavorites = ref(false)
const dailyRecommendations = ref([])
const favoriteList = ref([])

// 获取内容预览（前100个字符）
const getPreview = (content) => {
  if (!content) return ''
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/sample-essay/${id}`)
}

// 加载每日推荐
const loadDailyRecommendations = async () => {
  loading.value = true
  try {
    const response = await sampleEssayAPI.getAllEssays()
    // API拦截器已经返回了response.data，所以response就是数据数组
    dailyRecommendations.value = Array.isArray(response) ? response : []
  } catch (error) {
    console.error('加载每日推荐失败:', error)
    ElMessage.error('加载每日推荐失败')
  } finally {
    loading.value = false
  }
}

// 加载收藏榜单
const loadFavoriteList = async () => {
  loadingFavorites.value = true
  try {
    const response = await sampleEssayAPI.getTopFavoriteEssays()
    // API拦截器已经返回了response.data，所以response就是数据数组
    favoriteList.value = Array.isArray(response) ? response : []
  } catch (error) {
    console.error('加载收藏榜单失败:', error)
    ElMessage.error('加载收藏榜单失败')
  } finally {
    loadingFavorites.value = false
  }
}

onMounted(() => {
  loadDailyRecommendations()
  loadFavoriteList()
})
</script>

<style scoped>
.sample-essay-list {
  padding: 20px;
  background: white;
}

.section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.essay-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.essay-card {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
}

.essay-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.essay-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px 0;
  color: #303133;
}

.essay-author {
  font-size: 14px;
  color: #606266;
  margin: 0 0 10px 0;
}

.essay-preview {
  font-size: 14px;
  color: #909399;
  line-height: 1.6;
  margin: 0 0 15px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.essay-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.essay-tag {
  background: #ecf5ff;
  color: #409eff;
  border: none;
}

.essay-favorite {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 14px;
}
</style>

