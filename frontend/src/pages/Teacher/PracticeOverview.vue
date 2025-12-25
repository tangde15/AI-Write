<template>
  <div class="practice-overview">
    <div class="header">
      <h2>练习情况</h2>
      <div class="binding">
        <span>绑定码：</span>
        <el-tag type="success" v-if="bindingCode">{{ bindingCode }}</el-tag>
        <el-button type="primary" size="small" @click="resetCode" style="margin-left:8px;">重置绑定码</el-button>
      </div>
    </div>

    <el-card>
      <template #header>
        <div class="card-header">已绑定学生</div>
      </template>
      <el-table :data="students" v-loading="loading" style="width:100%;">
        <el-table-column label="用户名" prop="username" />
        <el-table-column label="年级" prop="grade" />
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewStudent(row)">查看详情</el-button>
            <el-button type="danger" size="small" @click="unbind(row)">解除绑定</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && students.length === 0" description="暂无绑定学生" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { teacherAPI } from '@/api/teacher'
import { ElMessage } from 'element-plus'

const router = useRouter()
const bindingCode = ref('')
const students = ref([])
const loading = ref(false)

const load = async () => {
  try {
    const codeRes = await teacherAPI.getBindingCode()
    bindingCode.value = codeRes.code
    students.value = await teacherAPI.getBoundStudents()
  } catch (e) {
    ElMessage.error('加载失败')
  }
}

const resetCode = async () => {
  const res = await teacherAPI.resetBindingCode()
  bindingCode.value = res.code
  ElMessage.success('绑定码已重置')
}

const viewStudent = (row) => {
  router.push(`/teacher/student/${row.id}/ability`)
}

const unbind = async (row) => {
  // 简化：暂不实现接口；后端已提供，可后续接入
  ElMessage.info('解除绑定功能后续接入')
}

onMounted(load)
</script>

<style scoped>
.practice-overview { padding: 16px; }
.header { display:flex; justify-content:space-between; align-items:center; margin-bottom: 12px; }
.binding { display:flex; align-items:center; }
.card-header { font-weight: 600; }
</style>
