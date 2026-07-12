<template>
  <div class="passengers-page">
    <header class="top-nav">
      <div class="nav-items">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/ticket" class="nav-item">车票查询</router-link>
        <router-link to="/passengers" class="nav-item active">乘车人管理</router-link>
      </div>
    </header>

    <div class="breadcrumb">
      当前位置：<a href="/">首页</a> &gt; <span class="current">乘车人管理</span>
    </div>

    <div class="content-wrapper">
      <aside class="sidebar">
        <div class="menu-section">
          <div class="menu-title">常用信息管理</div>
          <div class="menu-items">
            <router-link to="/passengers" class="menu-item active">乘车人</router-link>
          </div>
        </div>
      </aside>

      <main class="main-content">
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="请输入乘客姓名"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
          <el-button @click="handleSearch">查询</el-button>
        </div>

        <div class="table-wrapper">
          <div class="table-header">
            <table class="passenger-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>证件类型</th>
                  <th>证件号码</th>
                  <th>旅客类型</th>
                  <th>手机/电话</th>
                  <th>核验状态</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <div class="table-toolbar">
            <button class="add-btn" @click="showAddDialog">
              <span class="icon">⊕</span> 添加
            </button>
            <button class="batch-delete-btn">
              <span class="icon">⊗</span> 批量删除
            </button>
          </div>
          <table class="passenger-table">
            <tbody>
              <tr v-for="(row, index) in filteredPassengers" :key="row.id">
                <td>
                  <input type="checkbox" :value="row.id" v-model="selectedIds" />
                  {{ index + 1 }}
                </td>
                <td>{{ row.lastName + row.firstName }}</td>
                <td>{{ getIdTypeName(row.idType) }}</td>
                <td>{{ maskIdCard(row.idCard) }}</td>
                <td>{{ getPassengerTypeName(row.passengerType) }}</td>
                <td>{{ maskPhone(row.phone) }}</td>
                <td>
                  <span class="verified-icon">✓</span>
                </td>
                <td>
                  <button class="delete-btn" @click="handleDelete(row)">
                    <span class="icon">⊗</span>
                  </button>
                  <button class="edit-btn" @click="handleEdit(row)">
                    <span class="icon">✎</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <div v-if="filteredPassengers.length === 0 && !loading" class="no-data">
            暂无乘车人，请添加
          </div>
        </div>
      </main>
    </div>

    <nav class="bottom-nav">
      <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </router-link>
      <router-link to="/passengers" class="nav-item" :class="{ active: $route.path === '/passengers' }">
        <span class="nav-icon">👤</span>
        <span class="nav-text">乘车人</span>
      </router-link>
    </nav>

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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
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
const searchKeyword = ref('')
const selectedIds = ref<number[]>([])
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

const filteredPassengers = computed(() => {
  if (!searchKeyword.value) return passengers.value
  const keyword = searchKeyword.value.toLowerCase()
  return passengers.value.filter(p =>
    (p.lastName + p.firstName).toLowerCase().includes(keyword)
  )
})

onMounted(async () => {
  await loadPassengers()
})

const getMockPassengers = () => {
  return [
    { id: 1, lastName: '张', firstName: '伟', idType: 'ID_CARD', idCard: '110101199001011234', passengerType: 1, phone: '13800138000' },
    { id: 2, lastName: '李', firstName: '娜', idType: 'ID_CARD', idCard: '120102199202022345', passengerType: 1, phone: '13900139000' },
    { id: 3, lastName: '王', firstName: '小明', idType: 'ID_CARD', idCard: '130103201503033456', passengerType: 2, phone: '13700137000' },
    { id: 4, lastName: '刘', firstName: '小华', idType: 'ID_CARD', idCard: '140104201004044567', passengerType: 2, phone: '13600136000' },
    { id: 5, lastName: '陈', firstName: '静', idType: 'ID_CARD', idCard: '330105199805055678', passengerType: 3, phone: '13500135000' },
    { id: 6, lastName: '杨', firstName: '强', idType: 'ID_CARD', idCard: '320106199506066789', passengerType: 3, phone: '13400134000' },
    { id: 7, lastName: '赵', firstName: '军', idType: 'ID_CARD', idCard: '210107198507077890', passengerType: 4, phone: '13300133000' },
    { id: 8, lastName: '周', firstName: '芳', idType: 'HK_MACAO_PASS', idCard: 'H12345678', passengerType: 1, phone: '+85212345678' },
    { id: 9, lastName: '吴', firstName: '明', idType: 'TAIWAN_PASS', idCard: 'W87654321', passengerType: 1, phone: '+886912345678' },
    { id: 10, lastName: '孙', firstName: '丽', idType: 'CHINA_PASSPORT', idCard: 'E11223344', passengerType: 1, phone: '13200132000' }
  ]
}

const loadPassengers = async () => {
  loading.value = true
  try {
    const res = await passengerApi.getList()
    passengers.value = res.data || []
    if (passengers.value.length === 0) {
      passengers.value = getMockPassengers()
    }
  } catch (error) {
    console.error('加载失败:', error)
    passengers.value = getMockPassengers()
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
}

const maskIdCard = (idCard: string) => {
  if (!idCard) return ''
  if (idCard.length === 18) {
    return idCard.slice(0, 4) + '**********' + idCard.slice(-4)
  }
  return idCard.slice(0, 2) + '****' + idCard.slice(-2)
}

const maskPhone = (phone: string) => {
  if (!phone) return ''
  return phone.slice(0, 3) + '****' + phone.slice(-4)
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
    'HK_MACAO_PASS': '港澳居民来往内地通行证',
    'TAIWAN_PASS': '台湾居民来往大陆通行证',
    'CHINA_PASSPORT': '中国护照',
    'FOREIGN_PASSPORT': '外国护照'
  }
  return map[type] || type
}

const getPassengerTypeName = (type: number) => {
  const types = ['', '成人', '儿童', '学生', '残军']
  return types[type] || '成人'
}
</script>

<style scoped>
.passengers-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.top-nav {
  background: #1e3c72;
  padding: 10px 20px;
}

.nav-items {
  display: flex;
  gap: 30px;
}

.nav-item {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  padding: 5px 0;
}

.nav-item.active {
  background: #2a5298;
  padding: 5px 15px;
  border-radius: 3px;
}

.breadcrumb {
  padding: 10px 20px;
  font-size: 12px;
  color: #666;
  background: #fff;
}

.breadcrumb a {
  color: #1e88e5;
  text-decoration: none;
}

.breadcrumb .current {
  color: #333;
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  padding: 15px;
  gap: 15px;
}

.sidebar {
  width: 180px;
  background: #fff;
  border-radius: 5px;
  padding: 10px;
}

.menu-section {
  margin-bottom: 15px;
}

.menu-title {
  font-weight: bold;
  color: #333;
  padding: 5px 0;
  margin-bottom: 5px;
}

.menu-subtitle {
  font-size: 12px;
  color: #666;
  padding-left: 10px;
  margin-bottom: 3px;
}

.menu-items {
  display: flex;
  flex-direction: column;
}

.menu-item {
  padding: 6px 10px;
  font-size: 13px;
  color: #666;
  text-decoration: none;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item.active {
  background: #1e88e5;
  color: #fff;
}

.main-content {
  flex: 1;
  background: #fff;
  border-radius: 5px;
  padding: 15px;
}

.search-bar {
  margin-bottom: 15px;
}

.table-wrapper {
  border: 1px solid #ddd;
}

.table-header {
  background: #f5f7fa;
  border-bottom: 1px solid #ddd;
}

.table-toolbar {
  display: flex;
  gap: 15px;
  padding: 10px 15px;
  background: #f8fbff;
  border-bottom: 1px solid #e6f2ff;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: #67c23a;
  color: #fff;
  border: none;
  padding: 5px 15px;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
}

.add-btn .icon {
  font-size: 16px;
}

.batch-delete-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: #f56c6c;
  color: #fff;
  border: none;
  padding: 5px 15px;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
}

.batch-delete-btn .icon {
  font-size: 14px;
}

.passenger-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.passenger-table th {
  padding: 10px 15px;
  text-align: center;
  background: #f5f7fa;
  border-bottom: 1px solid #ddd;
  font-weight: normal;
  color: #666;
}

.passenger-table td {
  padding: 12px 15px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

.verified-icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  background: #67c23a;
  color: #fff;
  border-radius: 50%;
  line-height: 20px;
  font-size: 12px;
}

.delete-btn, .edit-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
  margin: 0 3px;
}

.delete-btn .icon {
  color: #f56c6c;
  font-size: 16px;
}

.edit-btn .icon {
  color: #409eff;
  font-size: 16px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
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
