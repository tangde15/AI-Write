<template>
  <div class="practice-page">
    <div class="practice-header">
      <h1 class="page-title">计算文学</h1>
      <div class="header-controls">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索题目"
          :prefix-icon="Search"
          class="search-input"
          clearable
          @change="handleSearch"
        />
        <el-select
          v-model="filterCondition"
          placeholder="筛选条件"
          class="filter-select"
          @change="handleFilter"
        >
          <el-option label="全部" value="all" />
          <el-option label="未完成" value="incomplete" />
          <el-option label="已完成" value="completed" />
        </el-select>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="practice-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="题库" name="library">
        <PracticeLibraryTab
          :keyword="searchKeyword"
          :filter="filterCondition"
          @view-book="handleViewBook"
        />
      </el-tab-pane>
      <el-tab-pane label="题单" name="set">
        <PracticeSetTab
          :keyword="searchKeyword"
          :filter="filterCondition"
          @view-book="handleViewBook"
        />
      </el-tab-pane>
      <el-tab-pane label="老师推送" name="pushed">
        <PushedBooksTab />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Search } from '@element-plus/icons-vue';
import PracticeLibraryTab from './PracticeLibraryTab.vue';
import PracticeSetTab from './PracticeSetTab.vue';
import PushedBooksTab from './PushedBooksTab.vue';

const router = useRouter();
const activeTab = ref('library');
const searchKeyword = ref('');
const filterCondition = ref('all');

const handleTabChange = (tab) => {
  console.log('切换标签页:', tab);
};

const handleSearch = () => {
  console.log('搜索:', searchKeyword.value);
};

const handleFilter = () => {
  console.log('筛选:', filterCondition.value);
};

const handleViewBook = (bookId) => {
  router.push(`/practice/book/${bookId}`);
};
</script>

<style scoped>
.practice-page {
  padding: 20px;
  background: #f5f5f9;
  min-height: 100vh;
}

.practice-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  color: white;
  font-size: 24px;
  margin: 0;
}

.header-controls {
  display: flex;
  gap: 15px;
}

.search-input {
  width: 250px;
}

.filter-select {
  width: 150px;
}

:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.9);
}

:deep(.el-select .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.9);
}

.practice-tabs {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-tabs__item.is-active) {
  color: #667eea;
}

:deep(.el-tabs__active-bar) {
  background-color: #667eea;
}
</style>
