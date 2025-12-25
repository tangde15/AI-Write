<template>
  <div class="ability-dashboard">
    <div class="header">
      <h2>个人能力</h2>
      <el-button type="primary" @click="goBack">返回首页</el-button>
    </div>

    <el-row :gutter="20" class="summary-row">
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">作文总数</div><div class="value">{{ summary.totalEssays }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">综合平均分</div><div class="value">{{ summary.avgScore }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">内容均分</div><div class="value">{{ summary.avgContent }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">结构均分</div><div class="value">{{ summary.avgStructure }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">语言均分</div><div class="value">{{ summary.avgLanguage }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">创意均分</div><div class="value">{{ summary.avgCreativity }}</div></div></el-card></el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">综合成长曲线</div>
          </template>
          <div ref="overallChart" style="width: 100%; height: 360px;"></div>
        </el-card>
      </el-col>

      <el-col :span="24" style="margin-top: 16px;">
        <el-card>
          <template #header>
            <div class="card-header">四维成长曲线（内容/结构/语言/创意）</div>
          </template>
          <div ref="dimensionChart" style="width: 100%; height: 420px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { abilityAPI } from '@/api/ability'
import { ElMessage } from 'element-plus'

const router = useRouter()
const summary = ref({ totalEssays: 0, avgScore: 0, avgContent: 0, avgStructure: 0, avgLanguage: 0, avgCreativity: 0 })
const timeseries = ref([])

const overallChart = ref(null)
const dimensionChart = ref(null)
let overallInstance = null
let dimensionInstance = null

const goBack = () => router.push('/student')

const loadData = async () => {
  try {
    console.log('[能力页] 开始加载数据...')
    summary.value = await abilityAPI.getSummary()
    console.log('[能力页] summary 加载成功:', summary.value)
    timeseries.value = await abilityAPI.getTimeseries()
    console.log('[能力页] timeseries 加载成功:', timeseries.value)
    renderCharts()
  } catch (e) {
    console.error('[能力页] 数据加载失败:', e)
    console.error('[能力页] 错误信息:', e.message || e.response?.data)
    ElMessage.error('加载个人能力数据失败：' + (e.response?.data?.message || e.message || '未知错误'))
  }
}

const renderCharts = () => {
  const dates = timeseries.value.map(p => p.date)
  const overallScores = timeseries.value.map(p => p.score || 0)

  // 综合曲线
  if (!overallInstance) overallInstance = echarts.init(overallChart.value)
  overallInstance.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', min: 0, max: 100 },
    series: [
      { name: '综合评分', type: 'line', data: overallScores, smooth: true }
    ]
  })

  // 四维曲线
  const content = timeseries.value.map(p => p.content || 0)
  const structure = timeseries.value.map(p => p.structure || 0)
  const language = timeseries.value.map(p => p.language || 0)
  const creativity = timeseries.value.map(p => p.creativity || 0)

  if (!dimensionInstance) dimensionInstance = echarts.init(dimensionChart.value)
  dimensionInstance.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['内容', '结构', '语言', '创意'] },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', min: 0 },
    series: [
      { name: '内容', type: 'line', data: content, smooth: true },
      { name: '结构', type: 'line', data: structure, smooth: true },
      { name: '语言', type: 'line', data: language, smooth: true },
      { name: '创意', type: 'line', data: creativity, smooth: true }
    ]
  })
}

const handleResize = () => {
  overallInstance && overallInstance.resize()
  dimensionInstance && dimensionInstance.resize()
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  overallInstance && overallInstance.dispose()
  dimensionInstance && dimensionInstance.dispose()
})
</script>

<style scoped>
.ability-dashboard { padding: 16px; }
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.summary-row { margin-bottom: 16px; }
.summary-item { text-align: center; }
.summary-item .label { color: #666; }
.summary-item .value { font-size: 20px; font-weight: 600; margin-top: 6px; }
.card-header { font-weight: 600; }
</style>
