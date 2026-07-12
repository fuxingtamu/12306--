<template>
  <div class="passengers-page">
    <!-- 头部 -->
    <header class="header">
      <router-link to="/" class="logo">12306旅客管理系统</router-link>
      <nav class="nav-links">
        <router-link to="/ticket">车票查询</router-link>
        <router-link to="/orders">订单管理</router-link>
      </nav>
    </header>

    <!-- 旅客列表 -->
    <div class="passengers-card card">
      <div class="header-row">
        <h2>常用联系人</h2>
        <el-button type="primary" @click="showAddDialog">添加旅客</el-button>
      </div>

      <el-table :data="passengers" v-loading="loading" stripe>
        <el-table-column label="姓名">
          <template #default="{ row }">
            {{ row.lastName + row.firstName }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号" />
        <el-table-column label="旅客类型">
          <template #default="{ row }">
            {{ getPassengerTypeName(row.passengerType) }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" />
        <el-table-column label="核验状态">
          <template #default="{ row }">
            <el-tag :type="row.isVerified ? 'success' : 'warning'">
              {{ row.isVerified ? '已核验' : '待核验' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="passengers.length === 0 && !loading" class="no-data">
        <el-empty description="暂无旅客信息，请添加" />
      </div>
    </div>

    <!-- 添加/编辑旅客对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑旅客' : '添加旅客'" width="500px">
      <el-form ref="formRef" :model="passengerForm" :rules="rules" label-width="100px">
        <el-form-item label="姓" prop="lastName">
          <el-input v-model="passengerForm.lastName" placeholder="请输入姓" maxlength="50" />
        </el-form-item>
        <el-form-item label="名" prop="firstName">
          <el-input v-model="passengerForm.firstName" placeholder="请输入名" maxlength="50" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="passengerForm.idCard" placeholder="请输入身份证号" maxlength="18" />
        </el-form-item>
        <el-form-item label="旅客类型">
          <el-select v-model="passengerForm.passengerType">
            <el-option label="成人" :value="1" />
            <el-option label="儿童" :value="2" />
            <el-option label="学生" :value="3" />
            <el-option label="残疾军人" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="passengerForm.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { passengerApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

interface Passenger {
  id: number
  lastName: string
  firstName: string
  idCard: string
  passengerType: number
  phone: string
  isVerified: number
}

const loading = ref(false)
const passengers = ref<Passenger[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInstance>()

const passengerForm = ref({
  lastName: '',
  firstName: '',
  idCard: '',
  passengerType: 1,
  phone: ''
})

const rules: FormRules = {
  lastName: [{ required: true, message: '请输入姓', trigger: 'blur' }],
  firstName: [{ required: true, message: '请输入名', trigger: 'blur' }],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

onMounted(async () => {
  await loadPassengers()
})

const loadPassengers = async () => {
  loading.value = true
  try {
    const res = await passengerApi.getList()
    passengers.value = res.data
  } catch (error) {
    console.error('加载失败')
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  editingId.value = null
  passengerForm.value = {
    lastName: '',
  firstName: '',
    idCard: '',
    passengerType: 1,
    phone: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row: Passenger) => {
  isEdit.value = true
  editingId.value = row.id
  passengerForm.value = {
    lastName: row.lastName,
    firstName: row.firstName,
    idCard: row.idCard,
    passengerType: row.passengerType,
    phone: row.phone || ''
  }
  dialogVisible.value = true
}

const handleDelete = async (row: Passenger) => {
  try {
    await ElMessageBox.confirm('确定要删除该旅客吗？', '提示', { type: 'warning' })
    await passengerApi.delete(row.id)
    ElMessage.success('删除成功')
    await loadPassengers()
  } catch (error) {
    console.error('删除失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (isEdit.value && editingId.value) {
      await passengerApi.update(editingId.value, passengerForm.value)
      ElMessage.success('修改成功')
    } else {
      await passengerApi.add(passengerForm.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    await loadPassengers()
  } catch (error) {
    console.error('操作失败')
  }
}

const getPassengerTypeName = (type: number) => {
  const types = ['', '成人', '儿童', '学生', '残军']
  return types[type] || '成人'
}
</script>

<style lang="scss" scoped>
.passengers-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .passengers-card {
    .header-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h2 {
        color: #3b99fc;
      }
    }
  }
}
</style>
