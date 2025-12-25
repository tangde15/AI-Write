<template>
  <div class="teacher-dashboard">
    <!-- 左侧导航栏 -->
    <div class="left-navbar">
      <div class="nav-header">
        <el-avatar>{{ userStore.username.charAt(0) }}</el-avatar>
        <div class="user-info">
          <div class="username">{{ userStore.username }}</div>
          <a href="#" @click.prevent="handleLogout" class="logout-link">退出登录</a>
        </div>
      </div>
      
      <el-menu
        :default-active="activeNav"
        class="nav-menu"
        background-color="#2c3e50"
        text-color="#ecf0f1"
        active-text-color="#3498db"
      >
        <el-menu-item index="home">
          <el-icon><HomeFilled /></el-icon>
          <span>推荐</span>
        </el-menu-item>
        
        <el-menu-item index="practice" @click="goPractice">
          <el-icon><Edit /></el-icon>
          <span>练习</span>
        </el-menu-item>
        
        <el-menu-item index="books" @click="goMyBooks">
          <el-icon><FolderOpened /></el-icon>
          <span>我的题单</span>
        </el-menu-item>
        
        <el-menu-item index="new-book" @click="goNewBook">
          <el-icon><CirclePlusFilled /></el-icon>
          <span>新增题单</span>
        </el-menu-item>
        
        <el-menu-item index="messages" @click="goToChat">
          <el-icon><ChatDotRound /></el-icon>
          <span>聊天</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 右侧内容区 -->
    <div class="content-area">
      <el-row :gutter="20">
      <!-- 每日推荐区域 -->
      <el-col :span="24">
        <div class="section-title">每日推荐</div>
        <el-row :gutter="20" style="margin-bottom: 40px;" v-loading="loadingEssays">
          <el-col :span="6" v-for="essay in dailyRecommendations" :key="essay.id" class="recommendation-card-col">
            <el-card class="recommendation-card" shadow="hover" @click="goToEssayDetail(essay.id)">
              <div class="card-content">
                <h3 class="card-title">{{ essay.title }}</h3>
                <p class="card-author">{{ essay.author }}</p>
                <p class="card-preview">{{ getPreview(essay.content) }}</p>
                <div class="card-footer">
                  <el-tag v-if="essay.tag" size="small">{{ essay.tag }}</el-tag>
                  <span class="card-rating">
                    <el-icon><Star /></el-icon>
                    {{ essay.favoriteCount || 0 }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24" v-if="!loadingEssays && dailyRecommendations.length === 0">
            <el-empty description="暂无推荐范文" :image-size="100" />
          </el-col>
        </el-row>
      </el-col>
      
      <!-- 收藏榜单区域 -->
      <el-col :span="24">
        <div class="section-title">收藏榜单</div>
        <el-row :gutter="20" v-loading="loadingFavorites">
          <el-col :span="6" v-for="essay in favoriteList" :key="essay.id" class="recommendation-card-col">
            <el-card class="recommendation-card" shadow="hover" @click="goToEssayDetail(essay.id)">
              <div class="card-content">
                <h3 class="card-title">{{ essay.title }}</h3>
                <p class="card-author">{{ essay.author }}</p>
                <p class="card-preview">{{ getPreview(essay.content) }}</p>
                <div class="card-footer">
                  <el-tag v-if="essay.tag" size="small">{{ essay.tag }}</el-tag>
                  <span class="card-rating">
                    <el-icon><Star /></el-icon>
                    {{ essay.favoriteCount || 0 }}
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24" v-if="!loadingFavorites && favoriteList.length === 0">
            <el-empty description="暂无收藏榜单" :image-size="100" />
          </el-col>
        </el-row>
      </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { sampleEssayAPI } from '@/api/sampleEssay'
import { ElMessage } from 'element-plus'
import { 
  HomeFilled, Edit, ChatDotRound, Star, FolderOpened, CirclePlusFilled
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const activeNav = ref('home')
const dailyRecommendations = ref([])
const favoriteList = ref([])
const loadingEssays = ref(false)
const loadingFavorites = ref(false)

const goToChat = () => {
  router.push('/chat')
}

const goPractice = () => {
  router.push('/teacher/practice')
}

const goMyBooks = () => {
  router.push('/teacher/books')
}

const goNewBook = () => {
  router.push('/teacher/books/new')
}

const showComingSoon = () => {
  ElMessage.info('功能开发中，敬请期待！')
}

// 获取内容预览
const getPreview = (content) => {
  if (!content) return ''
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

// 跳转到范文详情（功能待开发）
const goToEssayDetail = (id) => {
  ElMessage.info('范文详情功能开发中，敬请期待！')
}

// 加载每日推荐
const loadDailyRecommendations = async () => {
  loadingEssays.value = true
  try {
    console.log('[教师端] 开始加载每日推荐...')
    const response = await sampleEssayAPI.getAllEssays()
    console.log('[教师端] 每日推荐响应:', response)
    dailyRecommendations.value = Array.isArray(response) ? response : []
    console.log('[教师端] 每日推荐数据:', dailyRecommendations.value)
  } catch (error) {
    console.error('加载每日推荐失败:', error)
    console.error('错误详情:', error.response || error.message)
    ElMessage.error('加载每日推荐失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingEssays.value = false
  }
}

// 加载收藏榜单
const loadFavoriteList = async () => {
  loadingFavorites.value = true
  try {
    console.log('[教师端] 开始加载收藏榜单...')
    const response = await sampleEssayAPI.getTopFavoriteEssays()
    console.log('[教师端] 收藏榜单响应:', response)
    favoriteList.value = Array.isArray(response) ? response : []
    console.log('[教师端] 收藏榜单数据:', favoriteList.value)
  } catch (error) {
    console.error('加载收藏榜单失败:', error)
    console.error('错误详情:', error.response || error.message)
    ElMessage.error('加载收藏榜单失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingFavorites.value = false
  }
}

// 登出功能
const handleLogout = async () => {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('登出失败:', error)
    ElMessage.error('登出失败')
  }
}

onMounted(() => {
  loadDailyRecommendations()
  loadFavoriteList()
})

onUnmounted(() => {
  // 清理函数
})
</script>

<style scoped>
.teacher-dashboard {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 左侧导航栏 */
.left-navbar {
  width: 200px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  color: #ecf0f1;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
}

.nav-header {
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  background: rgba(52, 73, 94, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.username {
  font-weight: 600;
  color: #ffffff;
  font-size: 16px;
}

.logout-link {
  color: #87ceeb;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
  cursor: pointer;
}

.logout-link:hover {
  color: #b0e0e6;
  text-decoration: underline;
}

.nav-menu {
  flex: 1;
  border: none;
}

.nav-badge {
  margin-left: 8px;
}

/* 右侧内容区 */
.content-area {
  flex: 1;
  margin-left: 200px;
  padding: 20px;
  max-width: calc(100% - 200px);
}

/* 推荐卡片样式 */
.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.recommendation-card-col {
  margin-bottom: 20px;
}

.recommendation-card {
  height: 280px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.recommendation-card:hover {
  transform: translateY(-4px);
}

.card-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 8px 0;
}

.card-author {
  font-size: 13px;
  color: #909399;
  margin: 0 0 12px 0;
}

.card-preview {
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
  margin: 0 0 8px 0;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  white-space: pre-wrap;
  word-break: break-word;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.card-rating {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

@media (max-width: 768px) {
  .left-navbar {
    width: 100%;
    position: relative;
  }
  
  .content-area {
    margin-left: 0;
    max-width: 100%;
  }
  
  .recommendation-card-col {
    margin-bottom: 15px;
  }
}
</style>





