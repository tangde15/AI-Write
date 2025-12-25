<template>
  <div class="bind-teacher">
    <el-card>
      <template #header>
        <div class="card-header">绑定教师</div>
      </template>
      
      <el-alert v-if="isBound" type="success" :closable="false" style="margin-bottom: 16px;">
        <template #default>
          已绑定教师，老师可以查看你的学习进度和作文。
        </template>
      </el-alert>

      <el-form :model="form" label-width="100px">
        <el-form-item label="绑定码">
          <el-input 
            v-model="form.code" 
            placeholder="请输入教师提供的绑定码（6位字母/数字）"
            maxlength="6"
            @keyup.enter="submitBind"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitBind" :loading="binding">绑定教师</el-button>
        </el-form-item>
      </el-form>

      <el-steps :active="0" finish-status="success" align-center style="margin-top: 20px;">
        <el-step title="获取绑定码" description="向教师索要6位绑定码" />
        <el-step title="输入绑定码" description="在上方输入框输入绑定码（可重复绑定多位教师）" />
        <el-step title="完成绑定" description="点击绑定按钮即可" />
      </el-steps>

      <!-- 已绑定教师列表 -->
      <div style="margin-top: 24px;">
        <h4 style="margin-bottom: 12px;">已绑定教师</h4>
        <el-empty v-if="boundTeachers.length === 0" description="暂无绑定教师" />
        <el-table v-else :data="boundTeachers" size="small" style="width: 100%">
          <el-table-column prop="username" label="教师姓名" width="180" />
          <el-table-column prop="account" label="账号" width="180" />
          <el-table-column prop="bindingAt" label="绑定时间" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { teacherAPI } from '@/api/teacher'
import { studentAPI } from '@/api/student'
import { ElMessage } from 'element-plus'

const form = ref({ code: '' })
const binding = ref(false)
const boundTeachers = ref([])
const isBound = computed(() => boundTeachers.value.length > 0)

const loadBoundTeachers = async () => {
  try {
    const list = await studentAPI.getBoundTeachers()
    boundTeachers.value = Array.isArray(list) ? list : []
  } catch (e) {
    console.error('获取已绑定教师失败', e)
  }
}

const submitBind = async () => {
  if (!form.value.code.trim()) {
    ElMessage.warning('请输入绑定码')
    return
  }
  binding.value = true
  try {
    const code = form.value.code.trim().toUpperCase()
    await teacherAPI.bindByCode(code)
    ElMessage.success('绑定成功！教师将能看到你的学习进度')
    form.value.code = ''
    await loadBoundTeachers()
  } catch (e) {
    const msg = e?.response?.data?.message || '绑定码无效或教师未生成绑定码'
    ElMessage.error('绑定失败：' + msg)
    console.error('绑定失败', e)
  } finally {
    binding.value = false
  }
}

onMounted(async () => {
  await loadBoundTeachers()
})
</script>

<style scoped>
.bind-teacher { padding: 16px; }
.card-header { font-weight: 600; }
</style>
