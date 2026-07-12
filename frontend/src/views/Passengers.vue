<template>
  <div class="passengers-page">
    <!-- 头部 -->
    <header class="header">
      <router-link to="/" class="logo">中国铁路12306</router-link>
      <nav class="nav-links">
        <router-link to="/">首页</router-link>
        <router-link to="/ticket">车票查询</router-link>
      </nav>
    </header>

    <!-- 乘车人列表 -->
    <div class="passengers-card">
      <div class="header-row">
        <h2>乘车人管理</h2>
        <el-button type="primary" @click="showAddDialog">添加乘车人</el-button>
      </div>

      <el-table :data="passengers" v-loading="loading" stripe>
        <el-table-column label="姓名">
          <template #default="{ row }">
            {{ row.lastName + row.firstName }}
          </template>
        </el-table-column>
        <el-table-column label="证件类型">
          <template #default="{ row }">
            {{ getIdTypeName(row.idType) }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="证件号码" />
        <el-table-column label="旅客类型">
          <template #default="{ row }">
            {{ getPassengerTypeName(row.passengerType) }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="passengers.length === 0 && !loading" class="no-data">
        <el-empty description="暂无乘车人，请添加" />
      </div>
    </div>

    <!-- 添加/编辑乘车人对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑乘车人' : '添加乘车人'" width="500px">
      <el-form ref="formRef" :model="passengerForm" :rules="rules" label-width="100px">
        <el-form-item label="姓" prop="lastName">
          <el-input v-model="passengerForm.lastName" placeholder="请输入姓" maxlength="50" />
        </el-form-item>
        <el-form-item label="名" prop="firstName">
          <el-input v-model="passengerForm.firstName" placeholder="请输入名" maxlength="50" />
        </el-form-item>
        <el-form-item label="证件类型" prop="idType">
          <el-select v-model="passengerForm.idType" placeholder="请选择证件类型">
            <el-option label="居民身份证" value="ID_CARD" />
            <el-option label="港澳居民来往内地通行证" value="HK_MACAO_PASS" />
            <el-option label="台湾居民来往大陆通行证" value="TAIWAN_PASS" />
            <el-option label="中国护照" value="CHINA_PASSPORT" />
            <el-option label="外国护照" value="FOREIGN_PASSPORT" />
          </el-select>
        </el-form-item>
        <el-form-item label="证件号码" prop="idCard">
          <el-input v-model="passengerForm.idCard" placeholder="请输入证件号码" maxlength="20" />
        </el-form-item>
        <el-form-item label="旅客类型">
          <el-select v-model="passengerForm.passengerType">
            <el-option label="成人" :value="1" />
            <el-option label="儿童" :value="2" />
            <el-option label="学生" :value="3" />
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

    <!-- 底部导航 -->
    <nav class="bottom-nav">
      <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </router-link>
    </nav>
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
  idType: string
  idCard: string
  passengerType: number
  phone: string
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
  idType: 'ID_CARD',
  idCard: '',
  passengerType: 1,
  phone: ''
})

const rules: FormRules = {
  lastName: [{ required: true, message: '请输入姓', trigger: 'blur' }],
  firstName: [{ required: true, message: '请输入名', trigger: 'blur' }],
  idType: [{ required: true, message: '请选择证件类型', trigger: 'change' }],
  idCard: [{ required: true, message: '请输入证件号码', trigger: 'blur' }]
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
    idType: 'ID_CARD',
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
    idType: row.idType || 'ID_CARD',
    idCard: row.idCard,
    passengerType: row.passengerType,
    phone: row.phone || ''
  }
  dialogVisible.value = true
}

const handleDelete = async (row: Passenger) => {
  try {
    await ElMessageBox.confirm('确定要删除该乘车人吗？', '提示', { type: 'warning' })
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

const getIdTypeName = (type: string) => {
  const map: Record<string, string> = {
    'ID_CARD': '居民身份证',
    'HK_MACAO_PASS': '港澳通行证',
    'TAIWAN_PASS': '台湾通行证',
    'CHINA_PASSPORT': '中国护照',
    'FOREIGN_PASSPORT': '外国护照'
  }
  return map[type] || type
}

const getPassengerTypeName = (type: number) => {
  const types = ['', '成人', '儿童', '学生']
  return types[type] || '成人'
}
</script>

<style scoped>
.passengers-page {
  padding: 20px;
  padding-bottom: 80px;
}

.header {
  background: #1a365d;
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: -20px -20px 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 20px;
}

.nav-links a {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
}

.passengers-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-row h2 {
  color: #3b99fc;
  margin: 0;
}

.no-data {
  padding: 40px;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  justify-content: space-around;
  padding: 12px 0;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.bottom-nav .nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  color: #666;
}

.bottom-nav .nav-item .nav-icon {
  font-size: 22px;
}

.bottom-nav .nav-item .nav-text {
  font-size: 12px;
}

.bottom-nav .nav-item.active {
  color: #1e88e5;
}
</style>