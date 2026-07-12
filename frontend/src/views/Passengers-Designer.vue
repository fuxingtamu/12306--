<template>
  <div class="passengers-page">
    <!-- 顶部导航 -->
    <header class="top-nav">
      <div class="nav-container">
        <div class="nav-left">
          <a href="/" class="logo">
            <span class="logo-icon">🚄</span>
            <span class="logo-text">12306</span>
          </a>
          <nav class="main-nav">
            <router-link to="/">首页</router-link>
            <router-link to="/ticket">车票</router-link>
            <router-link to="/passengers" class="active">旅客</router-link>
            <router-link to="/orders">订单</router-link>
            <router-link to="/user">我的</router-link>
          </nav>
        </div>
        <div class="nav-right">
          <span class="user-name">我的账号</span>
        </div>
      </div>
    </header>

    <!-- 主内容 -->
    <main class="main-content">
      <div class="content-wrapper">
        <!-- 页面标题 -->
        <div class="page-header">
          <div class="header-left">
            <h1 class="page-title">常用联系人</h1>
            <span class="page-tip">最多可添加15位常用联系人</span>
          </div>
          <el-button type="primary" size="large" @click="showAddDialog">
            <span class="btn-icon">+</span> 添加旅客
          </el-button>
        </div>

        <!-- 旅客卡片列表 -->
        <div class="passenger-list">
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-list">
            <div v-for="i in 4" :key="i" class="skeleton-card"></div>
          </div>

          <!-- 空状态 -->
          <div v-else-if="passengers.length === 0" class="empty-state">
            <div class="empty-image">
              <svg viewBox="0 0 120 100" fill="none">
                <rect x="20" y="20" width="80" height="60" rx="4" fill="#e8f4fd" stroke="#1890ff" stroke-width="2"/>
                <circle cx="45" cy="45" r="10" fill="#1890ff" opacity="0.3"/>
                <path d="M35 65c0-5.5 4.5-10 10-10s10 4.5 10 10" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
                <line x1="60" y1="35" x2="80" y2="35" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
                <line x1="60" y1="45" x2="75" y2="45" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
                <line x1="60" y1="55" x2="70" y2="55" stroke="#1890ff" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
            <p class="empty-title">暂无常用联系人</p>
            <p class="empty-desc">添加常用联系人，购票更便捷</p>
            <el-button type="primary" @click="showAddDialog">立即添加</el-button>
          </div>

          <!-- 旅客卡片 -->
          <div v-else class="card-grid">
            <div
              v-for="p in passengers"
              :key="p.id"
              class="passenger-card"
            >
              <div class="card-top">
                <div class="passenger-avatar">
                  <span>{{ (p.lastName + p.firstName).charAt(0) }}</span>
                </div>
                <div class="passenger-info">
                  <div class="passenger-name">
                    {{ p.lastName + p.firstName }}
                    <span v-if="p.isDefault" class="default-tag">默认</span>
                  </div>
                  <div class="passenger-type" :class="`type-${p.passengerType}`">
                    {{ typeNames[p.passengerType] }}
                  </div>
                </div>
              </div>

              <div class="card-detail">
                <div class="detail-row">
                  <span class="detail-label">证件号码</span>
                  <span class="detail-value">{{ maskId(p.idCard) }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">手机号码</span>
                  <span class="detail-value">{{ p.phone || '-' }}</span>
                </div>
              </div>

              <div class="card-footer">
                <el-button size="small" @click="handleEdit(p)">编辑</el-button>
                <el-button size="small" type="danger" plain @click="handleDelete(p)">删除</el-button>
              </div>
            </div>

            <!-- 添加卡片 -->
            <div v-if="passengers.length < 8" class="add-card" @click="showAddDialog">
              <span class="add-icon">+</span>
              <span class="add-text">添加旅客</span>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑旅客' : '添加旅客'"
      width="520px"
      class="passenger-dialog"
    >
      <el-form ref="formRef" :model="passengerForm" :rules="rules" label-width="80px">
        <el-form-item label="姓" prop="lastName">
          <el-input v-model="passengerForm.lastName" placeholder="请输入姓" maxlength="50" />
        </el-form-item>

        <el-form-item label="名" prop="firstName">
          <el-input v-model="passengerForm.firstName" placeholder="请输入名" maxlength="50" />
        </el-form-item>

        <el-form-item label="证件类型" prop="idType">
          <el-select v-model="passengerForm.idType" placeholder="请选择证件类型" style="width: 100%">
            <el-option label="居民身份证" value="ID_CARD" />
            <el-option label="护照" value="PASSPORT" />
            <el-option label="港澳通行证" value="HK_MACAO" />
            <el-option label="台湾通行证" value="TAIWAN" />
          </el-select>
        </el-form-item>

        <el-form-item label="证件号码" prop="idCard">
          <el-input v-model="passengerForm.idCard" placeholder="请输入证件号码" maxlength="18" />
        </el-form-item>

        <el-form-item label="旅客类型" prop="passengerType">
          <el-radio-group v-model="passengerForm.passengerType">
            <el-radio :value="1">成人</el-radio>
            <el-radio :value="2">儿童</el-radio>
            <el-radio :value="3">学生</el-radio>
            <el-radio :value="4">残疾军人</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="手机号码">
          <el-input v-model="passengerForm.phone" placeholder="请输入手机号码（选填）" maxlength="11" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '保存' : '确认添加' }}
        </el-button>
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
  isDefault?: number
}

const loading = ref(false)
const submitting = ref(false)
const passengers = ref<Passenger[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInstance>()

const typeNames: Record<number, string> = { 1: '成人', 2: '儿童', 3: '学生', 4: '残疾军人' }

const passengerForm = ref({
  lastName: '',
  firstName: '',
  idType: 'ID_CARD',
  idCard: '',
  passengerType: 1,
  phone: ''
})

const rules: FormRules = {
  lastName: [
    { required: true, message: '请输入姓', trigger: 'blur' },
    { min: 1, max: 50, message: '姓长度为1-50个字符', trigger: 'blur' }
  ],
  firstName: [
    { required: true, message: '请输入名', trigger: 'blur' },
    { min: 1, max: 50, message: '名长度为1-50个字符', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入证件号码', trigger: 'blur' }
  ]
}

// Mock数据
const mockPassengers: Passenger[] = [
  { id: 1, lastName: '张', firstName: '三', idCard: '110101199001011234', passengerType: 1, phone: '13800138001', isVerified: 1, isDefault: 1 },
  { id: 2, lastName: '李', firstName: '小红', idCard: '310101199505055678', passengerType: 2, phone: '13800138002', isVerified: 1, isDefault: 0 },
  { id: 3, lastName: '王', firstName: '明', idCard: '440101200001011234', passengerType: 3, phone: '13800138003', isVerified: 0, isDefault: 0 },
]

let mockId = 4

onMounted(() => {
  loadPassengers()
})

const loadPassengers = async () => {
  loading.value = true
  // 使用Mock数据演示界面
  await new Promise(resolve => setTimeout(resolve, 500))
  passengers.value = [...mockPassengers]
  loading.value = false
}

const showAddDialog = () => {
  isEdit.value = false
  editingId.value = null
  passengerForm.value = { lastName: '', firstName: '', idType: 'ID_CARD', idCard: '', passengerType: 1, phone: '' }
  dialogVisible.value = true
}

const handleEdit = (row: Passenger) => {
  isEdit.value = true
  editingId.value = row.id
  passengerForm.value = {
    lastName: row.lastName,
    firstName: row.firstName,
    idType: 'ID_CARD',
    idCard: row.idCard,
    passengerType: row.passengerType,
    phone: row.phone || ''
  }
  dialogVisible.value = true
}

const handleDelete = async (row: Passenger) => {
  try {
    await ElMessageBox.confirm(`确定要删除旅客"${row.lastName + row.firstName}"吗？`, '提示', { type: 'warning' })
    // Mock删除
    passengers.value = passengers.value.filter(p => p.id !== row.id)
    ElMessage.success('删除成功')
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  submitting.value = true
  await new Promise(resolve => setTimeout(resolve, 300))
  try {
    if (isEdit.value && editingId.value) {
      // Mock更新
      const idx = passengers.value.findIndex(p => p.id === editingId.value)
      if (idx !== -1) {
        passengers.value[idx] = { ...passengers.value[idx], ...passengerForm.value }
      }
      ElMessage.success('修改成功')
    } else {
      // Mock添加
      const newPassenger: Passenger = {
        id: mockId++,
        lastName: passengerForm.value.lastName,
        firstName: passengerForm.value.firstName,
        idCard: passengerForm.value.idCard,
        passengerType: passengerForm.value.passengerType,
        phone: passengerForm.value.phone,
        isVerified: 1,
        isDefault: 0
      }
      passengers.value.push(newPassenger)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
  } catch {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

const maskId = (id: string) => {
  if (!id || id.length < 8) return id
  return id.slice(0, 4) + '********' + id.slice(-4)
}
</script>

<style lang="scss" scoped>
// 12306活力商务风格 - 蓝白主调，专业可信
$primary: #1890ff;
$primary-dark: #096dd9;
$success: #52c41a;
$warning: #faad14;
$danger: #ff4d4f;
$text-primary: #262626;
$text-secondary: #595959;
$text-muted: #8c8c8c;
$border: #d9d9d9;
$bg-gray: #f5f5f5;
$bg-white: #ffffff;

.passengers-page {
  min-height: 100vh;
  background: $bg-gray;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

// 顶部导航 - 12306风格
.top-nav {
  background: $bg-white;
  border-bottom: 1px solid $border;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;

  .nav-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
    height: 56px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .nav-left {
    display: flex;
    align-items: center;
    gap: 40px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 6px;
    text-decoration: none;

    .logo-icon {
      font-size: 24px;
    }

    .logo-text {
      font-size: 18px;
      font-weight: 700;
      color: $primary;
    }
  }

  .main-nav {
    display: flex;
    gap: 8px;

    a {
      padding: 8px 16px;
      color: $text-secondary;
      text-decoration: none;
      font-size: 14px;
      border-radius: 4px;
      transition: all 0.2s;

      &:hover {
        color: $primary;
        background: rgba($primary, 0.06);
      }

      &.active {
        color: $primary;
        background: rgba($primary, 0.1);
        font-weight: 500;
      }
    }
  }

  .nav-right {
    .user-name {
      font-size: 14px;
      color: $text-secondary;
    }
  }
}

// 主内容区
.main-content {
  padding: 24px;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

// 页面标题
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  .header-left {
    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: $text-primary;
      margin: 0 0 4px;
    }

    .page-tip {
      font-size: 12px;
      color: $text-muted;
    }
  }

  .btn-icon {
    margin-right: 6px;
    font-weight: 400;
  }
}

// 加载状态
.loading-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;

  .skeleton-card {
    height: 180px;
    background: $bg-white;
    border-radius: 4px;
    animation: skeleton-pulse 1.5s ease-in-out infinite;
  }
}

@keyframes skeleton-pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
}

// 空状态
.empty-state {
  text-align: center;
  padding: 60px 24px;
  background: $bg-white;
  border-radius: 4px;

  .empty-image {
    width: 120px;
    height: 100px;
    margin: 0 auto 16px;

    svg {
      width: 100%;
      height: 100%;
    }
  }

  .empty-title {
    font-size: 16px;
    color: $text-primary;
    margin: 0 0 8px;
  }

  .empty-desc {
    font-size: 14px;
    color: $text-muted;
    margin: 0 0 24px;
  }
}

// 卡片网格
.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

// 旅客卡片
.passenger-card {
  background: $bg-white;
  border-radius: 4px;
  border: 1px solid $border;
  overflow: hidden;
  transition: all 0.2s;

  &:hover {
    border-color: $primary;
    box-shadow: 0 4px 12px rgba($primary, 0.15);
  }

  .card-top {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px;
    border-bottom: 1px solid $bg-gray;
  }

  .passenger-avatar {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
    display: flex;
    align-items: center;
    justify-content: center;

    span {
      color: white;
      font-size: 18px;
      font-weight: 500;
    }
  }

  .passenger-info {
    flex: 1;
  }

  .passenger-name {
    font-size: 15px;
    font-weight: 600;
    color: $text-primary;
    display: flex;
    align-items: center;
    gap: 6px;

    .default-tag {
      font-size: 10px;
      padding: 2px 6px;
      background: $primary;
      color: white;
      border-radius: 2px;
      font-weight: 400;
    }
  }

  .passenger-type {
    font-size: 12px;
    margin-top: 4px;

    &.type-1 { color: $primary; }
    &.type-2 { color: $success; }
    &.type-3 { color: #722ed1; }
    &.type-4 { color: $danger; }
  }

  .card-detail {
    padding: 12px 16px;
  }

  .detail-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    font-size: 13px;

    &:last-child { margin-bottom: 0; }

    .detail-label {
      color: $text-muted;
    }

    .detail-value {
      color: $text-secondary;
      font-family: 'SF Mono', 'Consolas', monospace;
    }
  }

  .card-footer {
    padding: 12px 16px;
    border-top: 1px solid $bg-gray;
    display: flex;
    gap: 8px;

    .el-button {
      flex: 1;
    }
  }
}

// 添加卡片
.add-card {
  background: $bg-white;
  border: 2px dashed $border;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 180px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    border-color: $primary;
    background: rgba($primary, 0.02);

    .add-icon { color: $primary; }
    .add-text { color: $primary; }
  }

  .add-icon {
    font-size: 32px;
    color: $text-muted;
    line-height: 1;
    margin-bottom: 8px;
  }

  .add-text {
    font-size: 14px;
    color: $text-muted;
  }
}

// 对话框
:deep(.passenger-dialog) {
  .el-dialog {
    border-radius: 4px;
    overflow: hidden;
  }

  .el-dialog__header {
    padding: 16px 20px;
    background: $bg-gray;
    border-bottom: 1px solid $border;
    margin: 0;

    .el-dialog__title {
      font-size: 16px;
      font-weight: 600;
      color: $text-primary;
    }
  }

  .el-dialog__body {
    padding: 24px 20px;
  }

  .el-dialog__footer {
    padding: 16px 20px;
    border-top: 1px solid $border;
  }

  .el-form-item__label {
    color: $text-secondary;
    font-size: 14px;
  }

  .el-radio-group {
    .el-radio {
      margin-right: 16px;
    }
  }
}
</style>
