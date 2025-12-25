<template>
  <div class="practice-book-detail">
    <div class="detail-header">
      <el-button
        :icon="ArrowLeft"
        circle
        class="back-btn"
        @click="handleBack"
      />
      <h1 class="page-title">计算文学</h1>
    </div>

    <div class="detail-content">
      <div class="content-left">
        <div class="info-card">
          <div class="color-block" />
          <div class="info-stats">
            <h2 class="stats-title">答题信息</h2>
            <div class="stats-numbers">
              <span class="completed-count">{{ bookInfo.completedCount }}</span>
              <span class="separator">/</span>
              <span class="total-count">{{ bookInfo.totalCount }}</span>
            </div>
            <p class="stats-label">答题数</p>
          </div>
        </div>

        <div class="question-list-card">
          <h3 class="card-title">题目详情</h3>
          <el-table :data="questions" class="question-table" stripe>
            <el-table-column prop="title" label="题目" min-width="150" />
            <el-table-column prop="completionStatus" label="完成状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.completionStatus)" size="small">
                  {{ row.completionStatus }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="systemGrade" label="系统批改" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getGradeTagType(row.systemGrade)" size="small">
                  {{ row.systemGrade }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="teacherGrade" label="教师批改" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getGradeTagType(row.teacherGrade)" size="small">
                  {{ row.teacherGrade }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="得分" width="80" align="center" />
            <el-table-column label="操作" width="100" align="center" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleViewQuestion(row.id)">
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <div class="content-right">
        <div class="timeline-card">
          <h3 class="card-title">时间线</h3>
          <div class="timeline-chart" ref="chartEl">
            <div
              v-for="(point, index) in timelineData"
              :key="index"
              class="timeline-point"
              :style="{ left: point.x + '%', bottom: point.y + '%' }"
            >
              <div class="point-dot" />
              <div class="point-label">{{ point.label }}</div>
            </div>
            <svg class="timeline-line" width="100%" height="100%">
              <polyline
                :points="timelinePoints"
                fill="none"
                stroke="#667eea"
                stroke-width="2"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import { getPracticeBookDetail, getPracticeBookQuestions, getPracticeBookTimeline } from '@/api/practice';
import { studentBooksAPI } from '@/api/books';
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();

const bookInfo = ref({
  completedCount: 2899,
  totalCount: 3500
});

const questions = ref([]);
const timelineData = ref([]);
const chartEl = ref(null);
const chartSize = ref({ width: 0, height: 0 });

const calcSize = () => {
  if (chartEl.value) {
    chartSize.value = {
      width: chartEl.value.clientWidth || 300,
      height: chartEl.value.clientHeight || 300
    };
  }
};

const timelinePoints = computed(() => {
  const { width, height } = chartSize.value;
  if (!width || !height) return '';
  return timelineData.value
    .map(point => `${(point.x / 100) * width},${height - (point.y / 100) * height}`)
    .join(' ');
});

const fetchBookDetail = async () => {
  try {
    const bookId = route.params.id;
    try {
      const response = await getPracticeBookDetail(bookId);
      if (response && response.data) {
        bookInfo.value = {
          completedCount: response.data.completedCount ?? 0,
          totalCount: response.data.totalCount ?? 0
        };
        return;
      }
    } catch (e) {
      // ignore: 使用本地模拟
    }
    
    // 模拟数据
    bookInfo.value = {
      completedCount: 4,
      totalCount: 8
    };
  } catch (error) {
    console.error('获取练习册详情失败:', error);
  }
};

const fetchQuestions = async () => {
  const bookId = route.params.id;
  try {
    // 优先使用学生端推送题单详情接口
    const detail = await studentBooksAPI.detail(bookId);
    const data = detail?.data || detail;
    if (data && Array.isArray(data.questions)) {
      questions.value = data.questions.map(q => ({
        id: q.id,
        title: q.title,
        completionStatus: '未完成',
        systemGrade: '未批改',
        teacherGrade: '未批改',
        score: '-' 
      }));
      bookInfo.value = {
        completedCount: 0,
        totalCount: questions.value.length
      };
      return;
    }
  } catch (e) {
    // 忽略，尝试使用通用练习册接口
  }

  try {
    const response = await getPracticeBookQuestions(bookId);
    if (response && Array.isArray(response) && response.length > 0) {
      questions.value = response;
      bookInfo.value = {
        completedCount: 0,
        totalCount: response.length
      };
    } else {
      questions.value = [];
      bookInfo.value = { completedCount: 0, totalCount: 0 };
    }
  } catch (error) {
    console.error('❌ 获取题目列表失败:', error);
    questions.value = [];
    bookInfo.value = { completedCount: 0, totalCount: 0 };
  }
};

const fetchTimeline = async () => {
  try {
    const bookId = route.params.id;
    try {
      const response = await getPracticeBookTimeline(bookId);
      if (response && response.data && Array.isArray(response.data) && response.data.length) {
        timelineData.value = response.data;
        return;
      }
    } catch (e) {
      // ignore
    }
    
    // 模拟数据（模拟答题进度）
    timelineData.value = [
      { x: 10, y: 25, label: '题目1' },
      { x: 25, y: 45, label: '题目2' },
      { x: 40, y: 35, label: '题目3' },
      { x: 55, y: 60, label: '题目4' },
      { x: 70, y: 0, label: '题目5' },
      { x: 85, y: 0, label: '题目6' }
    ];
  } catch (error) {
    console.error('获取时间线数据失败:', error);
  }
};

const getStatusTagType = (status) => {
  return status === '已完成' ? 'success' : 'info';
};

const getGradeTagType = (grade) => {
  return grade === '已批改' ? 'success' : 'warning';
};

const handleBack = () => {
  router.back();
};

const handleViewQuestion = (questionId) => {
  router.push(`/practice/question/${questionId}`);
};

onMounted(() => {
  fetchBookDetail();
  fetchQuestions();
  fetchTimeline();
  setTimeout(calcSize, 0);
  window.addEventListener('resize', calcSize);
});
</script>

<style scoped>
.practice-book-detail {
  padding: 20px;
  background: #f5f5f9;
  min-height: 100vh;
}

.detail-header {
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

.detail-content {
  display: flex;
  gap: 20px;
}

.content-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.content-right {
  width: 350px;
}

.info-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  display: flex;
  align-items: center;
  gap: 30px;
}

.color-block {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
}

.info-stats {
  flex: 1;
}

.stats-title {
  font-size: 16px;
  color: #666;
  margin: 0 0 10px 0;
}

.stats-numbers {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 5px;
}

.completed-count {
  color: #667eea;
}

.separator {
  color: #999;
  margin: 0 5px;
}

.total-count {
  color: #333;
}

.stats-label {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.question-list-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
}

.question-table {
  width: 100%;
}

.timeline-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  height: fit-content;
}

.timeline-chart {
  position: relative;
  height: 300px;
  margin-top: 20px;
}

.timeline-line {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
}

.timeline-point {
  position: absolute;
  transform: translate(-50%, 50%);
}

.point-dot {
  width: 10px;
  height: 10px;
  background: #667eea;
  border: 2px solid white;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.point-label {
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 12px;
  color: #666;
  white-space: nowrap;
}
</style>
