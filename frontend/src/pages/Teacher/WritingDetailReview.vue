<template>
  <div class="writing-detail">
    <div class="header">
      <h2>作文详情与教师重批</h2>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-header">学生原作</div></template>
          <div class="meta"><strong>题目：</strong>{{ topic || '未命题' }}</div>
          <pre class="essay">{{ essay }}</pre>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-header">AI 批改结果（摘要）</div></template>
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
import api from '@/api/auth'

const route = useRoute()
const id = parseInt(route.params.id)

const essay = ref('')
const topic = ref('')
const aiBrief = ref('')
const form = ref({ content: 60, structure: 60, language: 60, creativity: 60, suggestions: '' })
const sections = ref({ content: '', structure: '', language: '', creativity: '' })

// 简化：拉取作文详情
const load = async () => {
  try {
    const detail = await api.get(`/writing/detail/${id}`)
    essay.value = detail.essay || ''
    topic.value = detail.topic || ''
    const ai = detail.aiResponse || ''
    aiBrief.value = ai.slice(0, 800)

    // 从AI文本解析四维分（30/20/30/20），换算到0-100
    const dims = extractDimensions(ai)
    if (dims.content != null) form.value.content = Math.round((dims.content / 30) * 100)
    if (dims.structure != null) form.value.structure = Math.round((dims.structure / 20) * 100)
    if (dims.language != null) form.value.language = Math.round((dims.language / 30) * 100)
    if (dims.creativity != null) form.value.creativity = Math.round((dims.creativity / 20) * 100)

    // 默认改进建议：教师已有建议优先，否则提取AI建议；再退化为AI摘要
    form.value.suggestions = detail.teacherFeedback || extractSuggestions(ai) || aiBrief.value
  } catch (e) {
    console.error(e)
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
  const get = (re) => {
    const m = text.match(re); return m ? parseInt(m[1], 10) : null
  }
  // 支持中英文冒号、加粗标记等
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

const submit = async () => {
  try {
    await teacherAPI.reviewWriting(id, form.value)
    ElMessage.success('重批完成，已同步学生端')
  } catch (e) {
    ElMessage.error('提交失败')
  }
}

load()
</script>

<style scoped>
.writing-detail { padding: 16px; }
.card-header { font-weight: 600; }
.essay { white-space: pre-wrap; line-height: 1.6; }
.ai { white-space: pre-wrap; line-height: 1.6; }
.sections { display: grid; grid-template-columns: 1fr; gap: 6px; width: 100%; }
</style>
