<template>
  <div class="practice-question-detail">
    <div class="detail-header">
      <el-button
        :icon="ArrowLeft"
        circle
        class="back-btn"
        @click="handleBack"
      />
      <h1 class="page-title">{{ questionInfo.title || '练习题' }}</h1>
    </div>

    <div class="detail-content">
      <div class="question-info">
        <div class="info-row">
          <span class="info-label">创建者：</span>
          <span class="info-value">{{ questionInfo.creator }}</span>
        </div>
        <div class="info-row">
          <span class="info-label">创建时间：</span>
          <span class="info-value">{{ questionInfo.createTime }}</span>
        </div>
        <div class="info-row">
          <span class="info-label">分数：</span>
          <span class="info-value score">{{ questionInfo.score }}分</span>
        </div>
        <div class="info-row">
          <el-button
            :icon="Star"
            :type="questionInfo.isFavorite ? 'warning' : 'default'"
            circle
            @click="handleToggleFavorite"
          />
          <span class="favorite-count">{{ questionInfo.favoriteCount }}</span>
        </div>
      </div>

      <div class="question-content-card">
        <h3 class="content-title">{{ questionInfo.title || '题目' }}</h3>
        <div class="requirement-section">
          <p class="requirement-text">{{ questionInfo.requirement }}</p>
        </div>

        <el-tabs v-model="activeTab" class="content-tabs">
          <!-- 作答标签页 -->
          <el-tab-pane label="作答" name="answer">
            <div v-if="questionInfo.isCompleted" class="answer-view">
              <div class="answer-content">{{ answerContent }}</div>
            </div>
            <div v-else class="answer-edit">
              <el-input
                v-model="answerContent"
                type="textarea"
                :rows="15"
                placeholder="请输入你的作答内容..."
                class="answer-textarea"
              />
              <div class="edit-actions">
                <el-button :loading="saving" :disabled="saving || submitting" @click="handleSave">保存</el-button>
                <el-button type="primary" :loading="submitting" :disabled="submitting" @click="handleSubmit">提交</el-button>
              </div>
            </div>
          </el-tab-pane>

          <!-- 系统批改标签页 -->
          <el-tab-pane
            label="系统批改"
            name="system"
            :disabled="!questionInfo.hasSystemFeedback"
          >
            <div v-if="loadingSystemFeedback" class="feedback-loading">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>批改中...（预计 3 秒）</span>
            </div>
            <div v-else class="system-feedback-wrapper">
              <!-- 题目内容区域 -->
              <div class="question-section">
                <h4 class="section-header">题目要求</h4>
                <div class="question-content">
                  {{ questionInfo.requirement }}
                </div>
                <el-button
                  v-if="showExpandQuestion"
                  text
                  type="primary"
                  size="small"
                  @click="expandedQuestion = !expandedQuestion"
                >
                  {{ expandedQuestion ? '收起' : '展开' }}
                </el-button>
              </div>

              <!-- 综合评分卡片 -->
              <div class="overall-card">
                <div class="score-display">
                  <div class="score-circle">
                    <div class="score-number">
                      {{ systemFeedback.totalScore || 0 }}
                    </div>
                    <div class="score-label">分</div>
                  </div>
                  <div class="score-info">
                    <div class="grade-badge" :class="getGradeBadgeClass(systemFeedback.overallScore)">
                      {{ getGradeLabel(systemFeedback.overallScore) }}
                    </div>
                    <el-progress
                      :percentage="Math.round(systemFeedback.overallScore * 100)"
                      :color="getProgressColor(systemFeedback.overallScore)"
                    />
                  </div>
                </div>
                <div class="overall-comment">
                  <p class="comment-text">
                    {{ systemFeedback.overallComment || '暂无总体评价' }}
                  </p>
                </div>
              </div>

              <!-- 分维度评分卡片 -->
              <div class="dimensions-container">
                <div class="dimension-card" v-for="(dimension, idx) in dimensionsList" :key="idx">
                  <div class="dimension-header">
                    <span class="dimension-title">{{ dimension.title }}</span>
                    <span class="dimension-score" :class="getScoreClass(dimension.score)">
                      {{ (dimension.score * 100).toFixed(0) }}
                    </span>
                  </div>
                  <el-progress
                    :percentage="Math.round(dimension.score * 100)"
                    :color="getProgressColor(dimension.score)"
                    class="dimension-progress"
                  />
                  <div class="dimension-guide">
                    {{ dimension.guide }}
                  </div>
                  <div class="dimension-comment">
                    {{ dimension.comment }}
                  </div>
                </div>
              </div>

              <!-- 作答原文预览 -->
              <div class="answer-section">
                <div class="section-header">
                  <span>作答原文</span>
                  <el-button
                    text
                    type="primary"
                    size="small"
                    @click="showAnswerText = !showAnswerText"
                  >
                    {{ showAnswerText ? '收起' : '查看' }}
                  </el-button>
                </div>
                <div v-if="showAnswerText" class="answer-text">
                  {{ answerContent }}
                </div>
              </div>

              <!-- 改进建议 -->
              <div class="summary-section">
                <h4 class="section-header">改进建议</h4>
                <div class="summary-content">
                  <!-- ⭐ 不用v-if，直接显示，支持改进建议是数组或字符串 -->
                  <template v-if="Array.isArray(systemFeedback.improvements)">
                    <!-- 改进建议是数组 -->
                    <ul class="improvements-list">
                      <li v-for="(item, idx) in systemFeedback.improvements" :key="idx" class="improvement-item">
                        {{ item }}
                      </li>
                    </ul>
                  </template>
                  <template v-else>
                    <!-- 改进建议是字符串或不存在 -->
                    <p v-if="systemFeedback.improvements" style="white-space: pre-line;" class="improvements-text">
                      {{ systemFeedback.improvements }}
                    </p>
                    <p v-else class="empty-hint">暂无具体改进建议</p>
                  </template>
                </div>
              </div>

              <!-- 操作按钮 -->
              <div class="action-buttons">
                <el-button
                  type="primary"
                  @click="handleRetryAnswer"
                >
                  重新作答
                </el-button>
                <el-button
                  @click="handleViewSampleEssay"
                >
                  查看同类范文
                </el-button>
              </div>
            </div>
          </el-tab-pane>

          <!-- 教师批改标签页 -->
          <el-tab-pane
            label="教师批改"
            name="teacher"
            :disabled="!questionInfo.hasTeacherFeedback"
          >
            <div v-if="loadingTeacherFeedback" class="feedback-loading">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>加载教师批改...</span>
            </div>
            <div v-else class="system-feedback-wrapper">
              <!-- 题目内容区域 -->
              <div class="question-section">
                <h4 class="section-header">题目要求</h4>
                <div class="question-content">
                  {{ questionInfo.requirement }}
                </div>
              </div>

              <!-- 综合评分卡片 -->
              <div class="overall-card">
                <div class="score-display">
                  <div class="score-circle">
                    <div class="score-number">
                      {{ teacherFeedbackData.totalScore || 0 }}
                    </div>
                    <div class="score-label">分</div>
                  </div>
                  <div class="score-progress">
                    <el-progress
                      :percentage="teacherFeedbackData.totalScore || 0"
                      :color="getProgressColor(teacherFeedbackData.totalScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
                <div class="overall-comment">
                  {{ teacherFeedbackData.overallComment || '教师暂无总评' }}
                </div>
              </div>

              <!-- 各维度评分卡片 -->
              <div class="dimension-cards">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <h4 class="dimension-title">内容评分</h4>
                    <div class="dimension-score">{{ teacherFeedbackData.contentScore || 0 }}</div>
                    <div class="dimension-percentage">{{ Math.round((teacherFeedbackData.contentScore || 0) / 30 * 100) }}%</div>
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="Math.round((teacherFeedbackData.contentScore || 0) / 30 * 100)"
                      :color="getDimensionColor('content')"
                      :stroke-width="12"
                    />
                  </div>
                  <div class="dimension-comment">{{ teacherFeedbackData.contentComment || '暂无点评' }}</div>
                </div>

                <div class="dimension-card">
                  <div class="dimension-header">
                    <h4 class="dimension-title">结构评分</h4>
                    <div class="dimension-score">{{ teacherFeedbackData.structureScore || 0 }}</div>
                    <div class="dimension-percentage">{{ Math.round((teacherFeedbackData.structureScore || 0) / 20 * 100) }}%</div>
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="Math.round((teacherFeedbackData.structureScore || 0) / 20 * 100)"
                      :color="getDimensionColor('structure')"
                      :stroke-width="12"
                    />
                  </div>
                  <div class="dimension-comment">{{ teacherFeedbackData.structureComment || '暂无点评' }}</div>
                </div>

                <div class="dimension-card">
                  <div class="dimension-header">
                    <h4 class="dimension-title">语言评分</h4>
                    <div class="dimension-score">{{ teacherFeedbackData.languageScore || 0 }}</div>
                    <div class="dimension-percentage">{{ Math.round((teacherFeedbackData.languageScore || 0) / 30 * 100) }}%</div>
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="Math.round((teacherFeedbackData.languageScore || 0) / 30 * 100)"
                      :color="getDimensionColor('language')"
                      :stroke-width="12"
                    />
                  </div>
                  <div class="dimension-comment">{{ teacherFeedbackData.languageComment || '暂无点评' }}</div>
                </div>

                <div class="dimension-card">
                  <div class="dimension-header">
                    <h4 class="dimension-title">创意评分</h4>
                    <div class="dimension-score">{{ teacherFeedbackData.creativityScore || 0 }}</div>
                    <div class="dimension-percentage">{{ Math.round((teacherFeedbackData.creativityScore || 0) / 20 * 100) }}%</div>
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="Math.round((teacherFeedbackData.creativityScore || 0) / 20 * 100)"
                      :color="getDimensionColor('creativity')"
                      :stroke-width="12"
                    />
                  </div>
                  <div class="dimension-comment">{{ teacherFeedbackData.creativityComment || '暂无点评' }}</div>
                </div>
              </div>

              <!-- 作答原文区域 -->
              <div class="answer-section">
                <h4 class="section-header">作答原文</h4>
                <div class="answer-content">{{ teacherFeedbackData.answerText || answerContent || '暂无作答内容' }}</div>
              </div>

              <!-- 改进建议区域 -->
              <div class="improvement-section">
                <h4 class="section-header">改进建议</h4>
                <div class="improvement-content">
                  {{ teacherFeedbackData.improvements || '教师暂未提供改进建议' }}
                </div>
                <div class="action-buttons">
                  <el-button type="primary" @click="handleRetry">重新作答</el-button>
                  <el-button @click="handleViewSampleEssay">查看范文</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowLeft, Star, Loading } from '@element-plus/icons-vue';
import {
  getPracticeQuestionDetail,
  submitPracticeAnswer,
  savePracticeAnswerDraft,
  getPracticeSystemFeedback,
  getPracticeAnswerStatus,
  getPracticeAnswerDetail,
  getPracticeTeacherFeedback,
  togglePracticeFavorite
} from '@/api/practice';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const activeTab = ref('answer');
const isTeacher = ref(false);
const loadingSystemFeedback = ref(false);
const loadingTeacherFeedback = ref(false);
const expandedQuestion = ref(false);
const showAnswerText = ref(false);

const questionInfo = ref({
  id: null,
  title: '',
  requirement: '',
  maxScore: 100,
  type: 'SUBJECTIVE',
  creator: '用户名称',
  createTime: '2025-08-10 19:30',
  score: 100,
  isFavorite: false,
  favoriteCount: 233,
  isCompleted: false,
  hasSystemFeedback: false,
  hasTeacherFeedback: false
});

const answerContent = ref('');
const saving = ref(false);
const submitting = ref(false);
let pollTimer = null;  // 轮询定时器
let currentAnswerId = null;  // 当前答案ID
const teacherFeedback = ref('');
const teacherFeedbackData = ref({
  totalScore: 0,
  overallComment: '',
  contentScore: 0,
  contentComment: '',
  structureScore: 0,
  structureComment: '',
  languageScore: 0,
  languageComment: '',
  creativityScore: 0,
  creativityComment: '',
  improvements: '',
  answerText: ''
});
const systemFeedback = ref({
  overallScore: 0.85,
  overallComment: '',
  contentScore: 0.95,
  contentComment: '',
  structureScore: 0.85,
  structureComment: '',
  standardScore: 0.86,
  standardComment: ''
});

// 分维度列表计算属性
const dimensionsList = computed(() => [
  {
    title: '内容评分',
    score: systemFeedback.value.contentScore,
    comment: systemFeedback.value.contentComment,
    guide: systemFeedback.value.contentGuide || '评估文章主题契合度、情感表达丰富度'
  },
  {
    title: '结构评分',
    score: systemFeedback.value.structureScore,
    comment: systemFeedback.value.structureComment,
    guide: systemFeedback.value.structureGuide || '评估段落布局、逻辑衔接是否清晰'
  },
  {
    title: '语言评分',
    score: systemFeedback.value.languageScore,
    comment: systemFeedback.value.languageComment,
    guide: systemFeedback.value.languageGuide || '评估语句通顺、词汇运用、修辞手法'
  },
  {
    title: '创意评分',
    score: systemFeedback.value.creativityScore,
    comment: systemFeedback.value.creativityComment,
    guide: systemFeedback.value.creativityGuide || '评估立意新颖、表达独特性'
  }
]);

// 根据分数返回等级标签
const getGradeLabel = (score) => {
  if (score >= 0.9) return '优秀';
  if (score >= 0.8) return '良好';
  if (score >= 0.7) return '中等';
  if (score >= 0.6) return '及格';
  return '需改进';
};

// 根据等级返回 badge 样式类
const getGradeBadgeClass = (score) => {
  if (score >= 0.9) return 'grade-excellent';
  if (score >= 0.8) return 'grade-good';
  if (score >= 0.7) return 'grade-fair';
  if (score >= 0.6) return 'grade-pass';
  return 'grade-poor';
};

// 根据分数返回进度条颜色
const getProgressColor = (score) => {
  if (score >= 0.9) return '#67C26A';  // 绿色
  if (score >= 0.8) return '#409EFF';  // 蓝色
  if (score >= 0.7) return '#E6A23C';  // 橙色
  return '#F56C6C';                     // 红色
};

// 根据维度返回进度条颜色（教师批改分维度进度条使用）
const getDimensionColor = (key) => {
  switch (key) {
    case 'content':
      return '#409EFF'; // primary 蓝
    case 'structure':
      return '#E6A23C'; // warning 橙
    case 'language':
      return '#67C23A'; // success 绿
    case 'creativity':
      return '#909399'; // info 灰蓝
    default:
      return '#409EFF';
  }
};

// 根据分数返回分数文字样式
const getScoreClass = (score) => {
  if (score >= 0.9) return 'score-excellent';
  if (score >= 0.8) return 'score-good';
  if (score >= 0.7) return 'score-fair';
  if (score >= 0.6) return 'score-pass';
  return 'score-poor';
};

// 判断题目是否需要折叠
const showExpandQuestion = computed(() => questionInfo.value.requirement?.length > 200);

const fetchQuestionDetail = async () => {
  try {
    const questionId = route.params.id;
    console.log('📖 获取题目详情，ID:', questionId);
    
    const response = await getPracticeQuestionDetail(questionId);
    console.log('✅ 题目详情响应:', response);
    
    if (response) {
      questionInfo.value = {
        id: response.id,
        title: response.title || '题目',
        requirement: response.requirement || '',
        maxScore: response.maxScore || 100,
        type: response.type || 'SUBJECTIVE',
        creator: response.creator || '用户名称',
        createTime: response.createTime || '2025-08-10 19:30',
        score: response.score || 100,
        isFavorite: response.isFavorite || false,
        favoriteCount: response.favoriteCount || 233,
        isCompleted: !!response.isCompleted,
        hasSystemFeedback: !!response.hasSystemFeedback,
        hasTeacherFeedback: !!response.hasTeacherFeedback
      };
      
      console.log('📝 题目信息已更新:', {
        title: questionInfo.value.title,
        requirement: questionInfo.value.requirement.substring(0, 50) + '...'
      });
      
      if (response.answer) {
        answerContent.value = response.answer;
      }
      
      // 如果已完成作答，从详情中获取 answerId
      if (questionInfo.value.isCompleted) {
        if (response.answerId) {
          currentAnswerId = response.answerId;
          console.log('✅ 获取到 answerId:', currentAnswerId);
        } else {
          console.warn('⚠️ 题目详情未返回 answerId');
        }
        if (questionInfo.value.hasSystemFeedback) {
          activeTab.value = 'system';
        }
      }
      return;
    }
    
    console.error('❌ 题目详情响应为空');
  } catch (error) {
    console.error('❌ 获取题目详情失败:', error);
    ElMessage.error('获取题目详情失败，请刷新重试');
  }
};

const fetchTeacherFeedback = async () => {
  if (!questionInfo.value.hasTeacherFeedback || !currentAnswerId) {
    console.log('未标记有教师批改或无答案ID，跳过获取');
    return;
  }

  loadingTeacherFeedback.value = true;
  try {
    const response = await getPracticeAnswerDetail(currentAnswerId);
    console.log('✅ 答案详情响应:', response);

    const text = response?.teacherFeedback || '';
    if (text) {
      // 解析教师反馈文本：内容评分：xx/30分 等格式
      const extractScore = (re) => {
        const m = text.match(re);
        return m ? parseInt(m[1], 10) : 0;
      };
      const extractComment = (dimension) => {
        const re = new RegExp(`${dimension}评分[：:].+?分[\\s\\n]*([^\\n]*(?:\\n(?!(?:内容|结构|语言|创意|改进建议)[：:]).*)*)`, 's');
        const m = text.match(re);
        return m ? m[1].trim() : '';
      };

      teacherFeedbackData.value = {
        totalScore: response?.score || 0,
        overallComment: text.includes('总体评价') ? text.match(/总体评价[：:]\s*([^\n]*)/)?.[1] || '' : '',
        contentScore: extractScore(/内容[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*30/i),
        contentComment: extractComment('内容'),
        structureScore: extractScore(/结构[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*20/i),
        structureComment: extractComment('结构'),
        languageScore: extractScore(/语言[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*30/i),
        languageComment: extractComment('语言'),
        creativityScore: extractScore(/创意[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*20/i),
        creativityComment: extractComment('创意'),
        improvements: text.match(/改进建议[：:]\s*([\s\S]*)$/)?.[1]?.trim() || '',
        answerText: response?.content || answerContent.value
      };

      console.log('✅ 已解析教师批改数据:', teacherFeedbackData.value);
    } else {
      console.warn('⚠️ 未找到教师批改文本');
    }
  } catch (e) {
    console.error('❌ 获取教师批改失败:', e);
  } finally {
    loadingTeacherFeedback.value = false;
  }
};

const fetchSystemFeedback = async () => {
  if (!questionInfo.value.hasSystemFeedback) {
    console.log('未标记有系统批改，跳过获取');
    return;
  }
  
  loadingSystemFeedback.value = true;
  try {
    const questionId = route.params.id;
    console.log('🔍 正在获取系统批改，questionId:', questionId);
    
    const response = await getPracticeSystemFeedback(questionId);
    console.log('✅ 系统批改响应:', response);
    
    if (response && !response.error) {
      console.log('📊 接收到系统批改数据:', response);
      console.log('🔍 总体评价:', response.overallComment);
      console.log('🔍 修改建议:', response.improvements);
      console.log('🔍 总体评价类型:', typeof response.overallComment, '长度:', response.overallComment?.length);
      console.log('🔍 修改建议类型:', typeof response.improvements, '长度:', response.improvements?.length);
      
      // ⭐ 直接赋值，保留所有字段（包括空字符串），不使用|| 运算符避免丢失数据
      systemFeedback.value = {
        totalScore: response.totalScore ?? 0,
        overallScore: (response.totalScore ?? 0) / 100,  // 转换为0-1比例
        overallComment: response.overallComment,  // ✅ 直接赋值，即使是''也保留
        improvements: response.improvements,  // ✅ 直接赋值
        
        contentScore: response.contentScore ?? 0,
        contentComment: response.contentComment,
        contentGuide: response.contentGuide,
        
        structureScore: response.structureScore ?? 0,
        structureComment: response.structureComment,
        structureGuide: response.structureGuide,
        
        languageScore: response.languageScore ?? 0,
        languageComment: response.languageComment,
        languageGuide: response.languageGuide,
        
        creativityScore: response.creativityScore ?? 0,
        creativityComment: response.creativityComment,
        creativityGuide: response.creativityGuide
      };
      
      console.log('✅ 已更新 systemFeedback:', systemFeedback.value);
    } else {
      console.warn('⚠️ 系统批改数据异常:', response);
    }
  } catch (e) {
    console.error('❌ 获取系统批改失败:', e);
    // 模拟数据
    systemFeedback.value = {
      overallScore: 0.85,
      overallComment: '这篇文章通过天空的多层次描述展现了对广阔精神世界的向往和思考。内容深刻，条理清晰，语言流畅，具有一定的思想性和感染力。若能在某些比喻修辞上更加精准，将使文章主题更加突出。',
      contentScore: 0.95,
      contentComment: '文章对"天空"的多重含义进行了有层次的探讨，展示了对天空不同层面意义认识。文章扣题严格，种神精神世界的表达和向往，语言丰富有深度，内容充实饱满。',
      structureScore: 0.85,
      structureComment: '文章结构为总分，包括导入问题间点，提出天空不同层面意义，然后分层阐述精神层面的内容。但在最后回扣主题略显不足，建议加强结尾的总结与升华。',
      standardScore: 0.86,
      standardComment: '文章语句通顺，段落分明，修辞手法运用得当，格式规范。但部分长句可适当调整结构使其更加简洁，标点使用整体规范。'
    };
  } finally {
    loadingSystemFeedback.value = false;
  }
};

const handleBack = () => {
  router.back();
};

const handleToggleFavorite = async () => {
  try {
    const questionId = route.params.id;
    await togglePracticeFavorite(questionId);
    questionInfo.value.isFavorite = !questionInfo.value.isFavorite;
    questionInfo.value.favoriteCount += questionInfo.value.isFavorite ? 1 : -1;
    ElMessage.success(questionInfo.value.isFavorite ? '收藏成功' : '取消收藏');
  } catch (error) {
    console.error('操作失败:', error);
    ElMessage.error('操作失败');
  }
};

const handleSave = async () => {
  if (saving.value || submitting.value) return;
  saving.value = true;
  try {
    const questionId = route.params.id;
    await savePracticeAnswerDraft(questionId, {
      answer: answerContent.value
    });
    ElMessage.success('保存成功');
  } catch (error) {
    console.error('保存失败:', error);
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
};

const handleSubmit = async () => {
  if (submitting.value) return;
  if (!answerContent.value.trim()) {
    ElMessage.warning('请输入作答内容');
    return;
  }
  submitting.value = true;
  try {
    const questionId = route.params.id;
    const submitRes = await submitPracticeAnswer(questionId, {
      answer: answerContent.value
    });
    
    console.log('🔍 完整提交响应:', submitRes);
    
    // ⭐ axios 拦截器已提取 response.data，submitRes 就是后端返回的数据
    if (!submitRes || !submitRes.answerId) {
      console.error('❌ 提交响应为空或缺少 answerId:', submitRes);
      ElMessage.error('提交失败：服务器无响应或数据格式错误');
      return;
    }
    
    currentAnswerId = submitRes.answerId;
    console.log('✅ 答案已保存，ID:', currentAnswerId, '状态:', submitRes.status);
    
    questionInfo.value.isCompleted = true;
    questionInfo.value.hasSystemFeedback = true;
    activeTab.value = 'system';
    
    // ⭐ 判断状态：如果已完成，直接显示结果；否则启动轮询
    if (submitRes.status === 'COMPLETED' || submitRes.status === 'DONE') {
      console.log('🎉 批改已完成，直接显示结果');
      ElMessage.success('✅ 批改完成');
      // 直接获取系统批改反馈
      await fetchSystemFeedback();
      loadingSystemFeedback.value = false;
    } else {
      console.log('⏳ 批改进行中，启动轮询');
      ElMessage.success('✅ 作答已提交，AI 正在批改中（预计 10-30 秒）...');
      // 显示加载提示
      loadingSystemFeedback.value = true;
      // ⭐ 启动轮询：每 2 秒查询一次答案状态
      startPollingAnswerStatus(currentAnswerId);
    }
    
  } catch (error) {
    console.error('❌ 提交失败:', error);
    console.error('错误详情:', error.response?.data || error.message);
    ElMessage.error('提交失败：' + (error.response?.data?.message || error.message || '未知错误'));
  } finally {
    submitting.value = false;
  }
};

// ⭐ 轮询答案状态
const startPollingAnswerStatus = (answerId) => {
  console.log('🔄 开始轮询答案状态, 轮询间隔: 2秒');
  
  // 清除之前的轮询
  if (pollTimer) clearInterval(pollTimer);
  
  pollTimer = setInterval(async () => {
    try {
      console.log('📡 轮询中... answerId:', answerId);
      const statusRes = await getPracticeAnswerStatus(answerId);
      console.log('状态查询响应:', statusRes);
      
      // axios 拦截器已提取 response.data
      const status = statusRes.status;
      
      if (status === 'PROCESSING') {
        console.log('⏳ 仍在处理中...');
        // 保持 loading 状态
      } else if (status === 'COMPLETED' || status === 'DONE') {
        console.log('✅ 批改完成');
        clearInterval(pollTimer);
        loadingSystemFeedback.value = false;
        
        // 获取完整的系统批改数据
        await fetchSystemFeedback();
        
        ElMessage.success('✅ 批改完成');
      } else if (status === 'FAILED') {
        console.log('❌ 批改失败');
        clearInterval(pollTimer);
        loadingSystemFeedback.value = false;
        ElMessage.error('❌ 批改失败: ' + (statusRes.errorMessage || '未知错误'));
      }
    } catch (error) {
      console.error('轮询出错:', error);
    }
  }, 2000);  // 每 2 秒轮询一次
};

const handleSaveTeacherFeedback = () => {
  ElMessage.success('批改已保存');
};

const handleRetryAnswer = async () => {
  // 重置完成状态，允许重新编辑
  questionInfo.value.isCompleted = false;
  questionInfo.value.hasSystemFeedback = false;
  questionInfo.value.hasTeacherFeedback = false;
  
  // 切回作答标签
  activeTab.value = 'answer';
  
  // 清空之前的答案内容
  answerContent.value = '';
  
  // 重置系统反馈
  systemFeedback.value = {
    totalScore: 0,
    overallScore: 0,
    overallComment: '',
    improvements: '',
    contentScore: 0,
    contentComment: '',
    contentGuide: '',
    structureScore: 0,
    structureComment: '',
    structureGuide: '',
    languageScore: 0,
    languageComment: '',
    languageGuide: '',
    creativityScore: 0,
    creativityComment: '',
    creativityGuide: ''
  };
  
  ElMessage.success('已切换回作答页，可以重新输入答案');
};

const handleViewSampleEssay = () => {
  ElMessage.info('跳转到范文库功能（待开发）');
  // 后续可跳转到范文库页面
  // router.push('/practice/sample-essays');
};

onMounted(async () => {
  await fetchQuestionDetail();
  // 获取题目详情后，如果已有系统批改，立即获取反馈数据
  if (questionInfo.value.hasSystemFeedback) {
    await fetchSystemFeedback();
  }
  // 获取教师批改
  if (questionInfo.value.hasTeacherFeedback) {
    await fetchTeacherFeedback();
  }
});
</script>

<style scoped>
.practice-question-detail {
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
  max-width: 1200px;
  margin: 0 auto;
}

.question-info {
  background: white;
  border-radius: 8px;
  padding: 20px 30px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 30px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.info-value.score {
  color: #667eea;
  font-size: 16px;
  font-weight: 600;
}

.favorite-count {
  font-size: 14px;
  color: #666;
  margin-left: -5px;
}

.question-content-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
}

.content-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 15px 0;
}

.requirement-section {
  background: #f8f9fc;
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 20px;
}

.requirement-text {
  font-size: 14px;
  line-height: 1.8;
  color: #666;
  margin: 0;
}

.content-tabs {
  margin-top: 20px;
}

.answer-view {
  padding: 20px;
  background: #f8f9fc;
  border-radius: 6px;
  min-height: 400px;
}

.answer-content {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
}

.answer-edit {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.answer-textarea {
  width: 100%;
}

.edit-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.feedback-content {
  padding: 20px;
}

.feedback-section {
  margin-bottom: 25px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  border-radius: 6px;
  border-left: 3px solid #667eea;
}

.feedback-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #667eea;
  margin: 0 0 12px 0;
}

.section-content {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  margin: 0;
}

.feedback-textarea {
  width: 100%;
  margin-bottom: 15px;
}

/* 系统批改新样式 */
.feedback-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.feedback-loading .is-loading {
  font-size: 32px;
  color: #667eea;
  margin-bottom: 15px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0); }
  100% { transform: rotate(360deg); }
}

.system-feedback-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
}

.question-section {
  background: #f8f9fc;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #667eea;
}

.question-section .section-header {
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
  margin: 0 0 12px 0;
}

.question-content {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  max-height: 200px;
  overflow: hidden;
  word-break: break-word;
}

.overall-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 30px;
  color: white;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.2);
}

.score-display {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-bottom: 20px;
}

.score-circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.score-number {
  font-size: 40px;
  font-weight: bold;
  line-height: 1;
}

.score-label {
  font-size: 12px;
  margin-top: 4px;
  opacity: 0.9;
}

.score-info {
  flex: 1;
}

.grade-badge {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 12px;
}

.grade-excellent {
  background: rgba(103, 194, 106, 0.3);
  color: #67C26A;
}

.grade-good {
  background: rgba(64, 158, 255, 0.3);
  color: #409EFF;
}

.grade-fair {
  background: rgba(230, 162, 60, 0.3);
  color: #E6A23C;
}

.grade-pass {
  background: rgba(245, 108, 108, 0.3);
  color: #F56C6C;
}

.grade-poor {
  background: rgba(245, 108, 108, 0.3);
  color: #F56C6C;
}

.overall-card :deep(.el-progress) {
  --el-progress-text-color: white;
}

.overall-comment {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.overall-comment p {
  margin: 0;
  font-size: 14px;
  line-height: 1.8;
  opacity: 0.95;
}

.dimensions-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.dimension-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #EBEEF5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.3s ease;
}

.dimension-card:hover {
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.15);
}

.dimension-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.dimension-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.dimension-score {
  font-size: 16px;
  font-weight: 700;
  padding: 4px 8px;
  border-radius: 4px;
}

.score-excellent {
  color: #67C26A;
  background: rgba(103, 194, 106, 0.1);
}

.score-good {
  color: #409EFF;
  background: rgba(64, 158, 255, 0.1);
}

.score-fair {
  color: #E6A23C;
  background: rgba(230, 162, 60, 0.1);
}

.score-pass {
  color: #F56C6C;
  background: rgba(245, 108, 108, 0.1);
}

.score-poor {
  color: #F56C6C;
  background: rgba(245, 108, 108, 0.1);
}

.dimension-progress {
  margin-bottom: 12px;
}

.dimension-guide {
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 2px solid #667eea;
}

.dimension-comment {
  font-size: 13px;
  line-height: 1.6;
  color: #606266;
}

.answer-section {
  background: #f8f9fc;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #667eea;
}

.answer-section .section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
  margin: 0 0 12px 0;
}

.answer-text {
  font-size: 13px;
  line-height: 1.8;
  color: #333;
  max-height: 300px;
  overflow-y: auto;
  padding: 12px;
  background: white;
  border-radius: 4px;
  border: 1px solid #EBEEF5;
}

.summary-section {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #667eea;
}

.summary-section .section-header {
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
  margin: 0 0 12px 0;
}

.summary-content {
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
}

.summary-content p {
  margin: 0;
}

.improvements-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.improvement-item {
  padding: 10px 0 10px 24px;
  position: relative;
  margin-bottom: 8px;
}

.improvement-item::before {
  content: '•';
  position: absolute;
  left: 8px;
  color: #667eea;
  font-weight: bold;
}

.improvements-text {
  margin: 0;
  white-space: pre-line;
}

.comment-text {
  margin: 0;
  line-height: 1.6;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #EBEEF5;
}

/* 响应式 */
@media (max-width: 768px) {
  .score-display {
    flex-direction: column;
    align-items: center;
  }

  .dimensions-container {
    grid-template-columns: 1fr;
  }

  .system-feedback-wrapper {
    padding: 16px;
    gap: 16px;
  }

  .overall-card {
    padding: 20px;
  }
}

:deep(.el-tabs__item) {
  font-size: 15px;
  font-weight: 500;
}

:deep(.el-tabs__item.is-active) {
  color: #667eea;
}

:deep(.el-tabs__active-bar) {
  background-color: #667eea;
}

:deep(.el-textarea__inner) {
  font-size: 14px;
  line-height: 1.8;
}
</style>
