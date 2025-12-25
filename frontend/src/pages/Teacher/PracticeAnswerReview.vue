<template>
  <div class="practice-detail">
    <div class="header"><h2>练习详情与教师重批</h2></div>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-header">题目与答案</div></template>
          <div class="meta"><strong>题目：</strong>{{ question?.title || '—' }}</div>
          <div class="req"><strong>要求：</strong><span v-html="(question?.requirement || '').replace(/\n/g,'<br/>')"></span></div>
          <el-divider/>
          <div class="ans-title">学生作答</div>
          <pre class="answer">{{ answer }}</pre>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-header">AI 批改（摘要）</div></template>
          <div class="ai">{{ aiBrief }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top:16px;">
      <template #header><div class="card-header">教师重批</div></template>
      <el-form :model="form" label-width="120px">
        <el-form-item label="内容分(0-100)"><el-input-number v-model="form.content" :min="0" :max="100" /></el-form-item>
        <el-form-item label="结构分(0-100)"><el-input-number v-model="form.structure" :min="0" :max="100" /></el-form-item>
        <el-form-item label="语言分(0-100)"><el-input-number v-model="form.language" :min="0" :max="100" /></el-form-item>
        <el-form-item label="创意分(0-100)"><el-input-number v-model="form.creativity" :min="0" :max="100" /></el-form-item>
        <el-form-item label="综合预览">
          <el-tag type="success">{{ previewOverall }}</el-tag>
          <span style="margin-left:8px;color:#888;">(按30/20/30/20加权)</span>
        </el-form-item>
        <el-form-item label="改进建议">
          <el-input type="textarea" v-model="form.suggestions" :rows="6" placeholder="按内容优化/结构调整/语言提升/创意拓展分点填写" />
        </el-form-item>
        <el-form-item label="分点输入">
          <div class="sections">
            <el-input v-model="sections.content" type="textarea" :rows="2" placeholder="内容优化" />
            <el-input v-model="sections.structure" type="textarea" :rows="2" placeholder="结构调整" />
            <el-input v-model="sections.language" type="textarea" :rows="2" placeholder="语言提升" />
            <el-input v-model="sections.creativity" type="textarea" :rows="2" placeholder="创意拓展" />
            <div style="margin-top:6px;">
              <el-button size="small" @click="composeSuggestions">一键排版到建议</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">完成重批</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { teacherAPI } from '@/api/teacher'

const route = useRoute()
const id = parseInt(route.params.id)

const question = ref(null)
const answer = ref('')
const aiBrief = ref('')
const form = ref({ content: 60, structure: 60, language: 60, creativity: 60, suggestions: '' })
const sections = ref({ content: '', structure: '', language: '', creativity: '' })

async function load() {
  try {
    const detail = await teacherAPI.getPracticeAnswerDetail(id)
    question.value = detail.question || {}
    answer.value = detail.content || ''
    const ai = detail.teacherFeedback && detail.teacherFeedback.trim().length > 0 ? detail.teacherFeedback : (detail.aiFeedback || '')
    aiBrief.value = (ai || '').slice(0, 800)

    // 优先从教师反馈解析四维，否则从AI反馈解析（30/20/30/20）
    const dims = extractDimensions(ai)
    if (dims.content != null) form.value.content = Math.round((dims.content / 30) * 100)
    if (dims.structure != null) form.value.structure = Math.round((dims.structure / 20) * 100)
    if (dims.language != null) form.value.language = Math.round((dims.language / 30) * 100)
    if (dims.creativity != null) form.value.creativity = Math.round((dims.creativity / 20) * 100)

    // 默认建议：已有教师建议优先，否则尝试从文本里提取
    form.value.suggestions = detail.teacherFeedback || extractSuggestions(ai) || aiBrief.value
  } catch (e) {
    console.error(e)
    ElMessage.error('加载练习详情失败')
  }
}

const previewOverall = computed(() => {
  const c = form.value.content || 0
  const s = form.value.structure || 0
  const l = form.value.language || 0
  const r = form.value.creativity || 0
  return Math.round((c * 30 + s * 20 + l * 30 + r * 20) / 100)
})

function extractDimensions(text) {
  if (!text) return {}
  const get = (re) => { const m = text.match(re); return m ? parseInt(m[1], 10) : null }
  return {
    content: get(/内容[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*30/i),
    structure: get(/结构[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*20/i),
    language: get(/语言[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*30/i),
    creativity: get(/创意[\u4e00-\u9fa5\w]*[：:]*\s*(?:评分|得分)?\s*\**\s*(\d{1,3})\s*\/\s*20/i)
  }
}

function extractSuggestions(text) {
  if (!text) return ''
  const m = text.match(/(?:改进建议|提升建议|建议)[:：]\s*([\s\S]*)$/)
  return m ? m[1].trim() : ''
}

function composeSuggestions() {
  const s = sections.value
  const blocks = []
  if (s.content) blocks.push(`内容优化：\n${s.content}`)
  if (s.structure) blocks.push(`结构调整：\n${s.structure}`)
  if (s.language) blocks.push(`语言提升：\n${s.language}`)
  if (s.creativity) blocks.push(`创意拓展：\n${s.creativity}`)
  form.value.suggestions = blocks.join('\n\n')
}

async function submit() {
  try {
    await teacherAPI.reviewPracticeAnswer(id, form.value)
    ElMessage.success('重批完成，已同步学生端')
  } catch (e) {
    console.error(e)
    ElMessage.error('提交失败')
  }
}

load()
</script>

<style scoped>
.practice-detail { padding: 16px; }
.card-header { font-weight: 600; }
.answer, .ai { white-space: pre-wrap; line-height: 1.6; }
.req { margin-top: 6px; white-space: pre-wrap; }
.sections { display: grid; grid-template-columns: 1fr; gap: 6px; width: 100%; }
</style>
