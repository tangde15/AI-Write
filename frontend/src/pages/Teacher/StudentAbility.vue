<template>
  <div class="student-ability">
    <div class="header">
      <h2>学生能力追踪</h2>
      <div>
        <el-button size="small" @click="setRange('week')">最近一周</el-button>
        <el-button size="small" @click="setRange('month')">最近一月</el-button>
        <el-button size="small" @click="setRange('term')">本学期</el-button>
        <el-button size="small" @click="setRange(null)" type="primary">全部</el-button>
      </div>
    </div>

    <el-row :gutter="20" class="summary-row">
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">作文总数</div><div class="value">{{ summary.totalEssays }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">综合平均分</div><div class="value">{{ summary.avgScore }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">内容均分</div><div class="value">{{ summary.avgContent }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">结构均分</div><div class="value">{{ summary.avgStructure }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">语言均分</div><div class="value">{{ summary.avgLanguage }}</div></div></el-card></el-col>
      <el-col :span="4"><el-card><div class="summary-item"><div class="label">创意均分</div><div class="value">{{ summary.avgCreativity }}</div></div></el-card></el-col>
    </el-row>

    <el-card>
      <template #header><div class="card-header">综合成长曲线</div></template>
      <div ref="overallChart" style="width:100%;height:360px"></div>
    </el-card>

    <el-card style="margin-top:16px;">
      <template #header><div class="card-header">四维成长曲线（内容/结构/语言/创意）</div></template>
      <div ref="dimensionChart" style="width:100%;height:420px"></div>
    </el-card>

    <el-card style="margin-top:16px;">
      <template #header>
        <div class="card-header" style="display:flex;justify-content:space-between;align-items:center;">
          <span>学生提交作文</span>
          <el-link type="primary" @click="toggleShowAll">{{ showAll ? '收起' : '查看全部' }}</el-link>
        </div>
      </template>
      <el-empty v-if="submissions.length === 0" description="暂无提交" />
      <el-collapse v-else>
        <el-collapse-item v-for="w in visibleSubmissions" :key="w.id + '-' + w.type" :name="w.id + '-' + w.type">
          <template #title>
            <div style="display:flex;gap:12px;align-items:center;">
              <el-tag size="small" :type="w.type==='WRITING' ? 'primary' : 'info'">{{ w.type==='WRITING' ? '作文' : '练习' }}</el-tag>
              <span>{{ w.date }}</span>
              <span style="font-weight:600;">{{ w.title }}</span>
              <el-tag type="success" v-if="w.score != null">得分：{{ w.score }}</el-tag>
            </div>
          </template>
          <div class="essay-brief">{{ (w.brief || '').slice(0, 160) }}{{ (w.brief || '').length > 160 ? '…' : '' }}</div>
          <div style="margin-top:8px;">
            <el-button v-if="w.type==='WRITING'" type="primary" size="small" @click="openDetail(w.id)">查看与重批</el-button>
            <el-button v-else type="primary" size="small" @click="openPracticeDetail(w.id)">查看与重批</el-button>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import * as echarts from 'echarts'
import { teacherAPI } from '@/api/teacher'
import { ElMessage } from 'element-plus'

const route = useRoute()
const studentId = ref(parseInt(route.params.id))
const range = ref(null)
const summary = ref({ totalEssays:0, avgScore:0, avgContent:0, avgStructure:0, avgLanguage:0, avgCreativity:0 })
const timeseries = ref([])
const submissions = ref([])
const showAll = ref(false)

const overallChart = ref(null)
const dimensionChart = ref(null)
let overallInstance = null
let dimensionInstance = null

const load = async () => {
  try {
    summary.value = await teacherAPI.getStudentSummary(studentId.value)
    timeseries.value = await teacherAPI.getStudentTimeseries(studentId.value, range.value)
    submissions.value = await teacherAPI.getStudentSubmissions(studentId.value)
    renderCharts()
  } catch(e) {
    ElMessage.error('加载学生能力失败')
    console.error(e)
  }
}

const renderCharts = () => {
  const dates = timeseries.value.map(p => p.date)
  const overall = timeseries.value.map(p => p.score || 0)
  if (!overallInstance) overallInstance = echarts.init(overallChart.value)
  overallInstance.setOption({ tooltip:{trigger:'axis'}, xAxis:{type:'category', data:dates}, yAxis:{type:'value', min:0, max:100}, series:[{ name:'综合评分', type:'line', data:overall, smooth:true }] })

  const content = timeseries.value.map(p => p.content || 0)
  const structure = timeseries.value.map(p => p.structure || 0)
  const language = timeseries.value.map(p => p.language || 0)
  const creativity = timeseries.value.map(p => p.creativity || 0)
  if (!dimensionInstance) dimensionInstance = echarts.init(dimensionChart.value)
  dimensionInstance.setOption({ tooltip:{trigger:'axis'}, legend:{data:['内容','结构','语言','创意']}, xAxis:{type:'category', data:dates}, yAxis:{type:'value', min:0}, series:[
    { name:'内容', type:'line', data:content, smooth:true },
    { name:'结构', type:'line', data:structure, smooth:true },
    { name:'语言', type:'line', data:language, smooth:true },
    { name:'创意', type:'line', data:creativity, smooth:true }
  ] })
}

const setRange = (r) => { range.value = r; load() }

const openDetail = (id) => { window.location.href = `/teacher/writing/${id}` }
const openPracticeDetail = (id) => { window.location.href = `/teacher/practice/answer/${id}` }
const toggleShowAll = () => { showAll.value = !showAll.value }
const visibleSubmissions = computed(() => showAll.value ? submissions.value : submissions.value.slice(0, 5))

onMounted(load)

watch(() => route.params.id, (nv) => { studentId.value = parseInt(nv); load() })
</script>

<style scoped>
.student-ability { padding: 16px; }
.header { display:flex; justify-content:space-between; align-items:center; margin-bottom: 12px; }
.summary-row { margin-bottom: 16px; }
.summary-item { text-align:center; }
.summary-item .label { color:#666; }
.summary-item .value { font-size:20px; font-weight:600; margin-top:6px; }
.card-header { font-weight:600; }
.essay-brief { white-space: pre-wrap; color:#555; }
</style>
