<template>
  <div class="home-page">
    <!-- 顶部导航 -->
    <header class="top-nav">
      <div class="logo-section">
        <span class="logo-icon">🚂</span>
        <span class="logo-text">中国铁路12306</span>
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-item active">首页</router-link>
        <router-link to="/passengers" class="nav-item">乘车人</router-link>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="main-content">
      <div class="search-box">
        <div class="search-tabs">
          <button class="tab-btn active">单程</button>
        </div>

        <div class="search-form">
          <div class="form-row">
            <div class="form-item">
              <label>出发地</label>
              <StationSelector 
                :selected-id="startStationId" 
                @change="handleStartStationChange"
                placeholder="请输入出发站"
              />
            </div>
            <div class="swap-btn" @click="swapStations">↔</div>
            <div class="form-item">
              <label>到达地</label>
              <StationSelector 
                :selected-id="endStationId" 
                @change="handleEndStationChange"
                placeholder="请输入到达站"
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-item">
              <label>出发日期</label>
              <el-date-picker 
                v-model="travelDate" 
                type="date" 
                placeholder="选择日期"
                :disabled-date="disabledDate"
                value-format="YYYY-MM-DD"
              />
            </div>
          </div>

          <button class="search-btn" @click="handleSearch">查询</button>
        </div>
      </div>
    </main>

    <!-- 底部导航 -->
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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import StationSelector from '@/components/StationSelector.vue'

const router = useRouter()

const startStationId = ref('')
const endStationId = ref('')
const startStationName = ref('')
const endStationName = ref('')
const travelDate = ref('')

const disabledDate = (date: Date) => {
  return date < new Date(new Date().toDateString())
}

const handleStartStationChange = (station: any) => {
  if (station) {
    startStationId.value = station.id
    startStationName.value = station.name
  }
}

const handleEndStationChange = (station: any) => {
  if (station) {
    endStationId.value = station.id
    endStationName.value = station.name
  }
}

const swapStations = () => {
  const tempId = startStationId.value
  startStationId.value = endStationId.value
  endStationId.value = tempId
  
  const tempName = startStationName.value
  startStationName.value = endStationName.value
  endStationName.value = tempName
}

const handleSearch = () => {
  if (!startStationId.value || !endStationId.value || !travelDate.value) {
    alert('请填写完整的查询信息')
    return
  }
  router.push({
    path: '/ticket',
    query: {
      startStation: startStationId.value,
      endStation: endStationId.value,
      startStationName: startStationName.value,
      endStationName: endStationName.value,
      travelDate: travelDate.value
    }
  })
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
}

.top-nav {
  background: #1a365d;
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.nav-links {
  display: flex;
  gap: 20px;
}

.nav-links .nav-item {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  padding: 4px 8px;
}

.nav-links .nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

.main-content {
  padding: 60px 20px 80px;
  display: flex;
  justify-content: center;
}

.search-box {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  width: 100%;
  max-width: 600px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.search-tabs {
  margin-bottom: 20px;
}

.tab-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  background: #1e88e5;
  color: #fff;
  cursor: pointer;
  font-size: 14px;
}

.search-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.form-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-item label {
  font-size: 12px;
  color: #666;
}

.swap-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
  cursor: pointer;
  font-size: 18px;
  color: #666;
}

.swap-btn:hover {
  background: #e0e0e0;
}

.search-btn {
  background: #f57c00;
  color: #fff;
  border: none;
  padding: 14px;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}

.search-btn:hover {
  background: #e65100;
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