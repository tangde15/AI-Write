<template>
  <div class="essay-detail">
    <div class="detail-container" v-loading="loading">
      <div class="back-button" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        <span>返回</span>
      </div>
      
      <div class="essay-content" v-if="essay">
        <div class="essay-header">
          <h1 class="essay-title">{{ essay.title }}</h1>
          <p class="essay-author">{{ essay.author }}</p>
        </div>
        
        <div class="essay-body">
          <div class="essay-text">{{ essay.content }}</div>
        </div>
        
        <div class="essay-footer">
          <el-tag v-if="essay.tag" size="large" class="essay-tag">{{ essay.tag }}</el-tag>
          <div class="essay-actions">
            <el-button 
              type="primary" 
              :icon="Star" 
              @click="handleFavorite"
              :loading="favoriting"
            >
              收藏 ({{ essay.favoriteCount || 0 }})
            </el-button>
          </div>
        </div>
      </div>
      
      <div v-else-if="!loading" class="empty-state">
        <p>范文不存在</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Star } from '@element-plus/icons-vue'
import { sampleEssayAPI } from '@/api/sampleEssay'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const favoriting = ref(false)
const essay = ref(null)

// 返回上一页
const goBack = () => {
  router.back()
}

// 收藏
const handleFavorite = async () => {
  if (!essay.value) return
  
  favoriting.value = true
  try {
    await sampleEssayAPI.incrementFavorite(essay.value.id)
    essay.value.favoriteCount = (essay.value.favoriteCount || 0) + 1
    ElMessage.success('收藏成功')
  } catch (error) {
    console.error('收藏失败:', error)
    ElMessage.error('收藏失败')
  } finally {
    favoriting.value = false
  }
}

// 加载范文详情
const loadEssayDetail = async () => {
  const id = route.params.id
  if (!id) {
    ElMessage.error('范文ID不存在')
    return
  }
  
  loading.value = true
  try {
    const response = await sampleEssayAPI.getEssayById(id)
    // API拦截器已经返回了response.data，所以response就是数据对象
    essay.value = response
  } catch (error) {
    console.error('加载范文详情失败:', error)
    ElMessage.error('加载范文详情失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadEssayDetail()
})
</script>

<style scoped>
.essay-detail {
  padding: 20px;
  background: white;
  min-height: 100vh;
}

.detail-container {
  max-width: 900px;
  margin: 0 auto;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #606266;
  margin-bottom: 20px;
  font-size: 14px;
  transition: color 0.3s;
}

.back-button:hover {
  color: #409eff;
}

.essay-content {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 40px;
}

.essay-header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.essay-title {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 15px 0;
  color: #303133;
}

.essay-author {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.essay-body {
  margin-bottom: 30px;
}

.essay-text {
  font-size: 16px;
  line-height: 2;
  color: #303133;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.essay-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.essay-tag {
  background: #ecf5ff;
  color: #409eff;
  border: none;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 100px 20px;
  color: #909399;
}
</style>

